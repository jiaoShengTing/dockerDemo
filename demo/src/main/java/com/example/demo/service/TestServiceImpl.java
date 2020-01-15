package com.example.demo.service;

import com.example.demo.dao.CofOrgRepository;
import com.example.demo.model.CofOrg;
import com.example.demo.provider.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TestServiceImpl implements TestService {
    private static Logger logger = LoggerFactory.getLogger(TestService.class);
    @PersistenceContext //注入的是实体管理器,执行持久化操作
    EntityManager entityManager;
    @Autowired
    private CofOrgRepository cofOrgRepository;

    private List<List<String>> codeList = new ArrayList<>();
    List<String> returnCode = new ArrayList<>();

    @Override
    public Map<String,Map<String,String>> testQuery(Integer level,String year) {
        Query psuCountQuery = null;
        List<CofOrg> cofOrgs =  cofOrgRepository.findAllByParentOrgCodeAndSysCode("org-SPIC_SZYD-000000","NEW-SPIC-SZYD");
        List<String> orgCode1 = cofOrgs.stream().map(CofOrg::getCode).collect(Collectors.toList());
        Map<String,Map<String,String>> map = new HashMap<>();
        int totalCom = 1;
        int totalPushCom = 1;

        /**
         * 目前查询到四级单位 需要查询五六级 更改循环参数
         * 一级：
         */
        for (int i = 2;i<=level;i++){
            ///查询已上报单位SQL
            StringBuffer sql = new StringBuffer("SELECT distinct company_id,sc.org_code,co.`NAME` FROM szyd_put_bitem spb " +
                    "LEFT JOIN szyd_company sc ON sc.id = spb.company_id "  +
                    "LEFT JOIN cof_org co ON co.`CODE` = sc.org_code " +
                   "WHERE sc.decision_flag = 0 AND sc.item_flag = 0 AND spb.oper_type != 'del' AND sc.org_code  IN (");
            ///查询需要上报单位SQL
            StringBuffer sql2 = new StringBuffer(" SELECT " +
                    "        distinct sc.id," +
                    "        sc.org_code," +
                    "        co.`NAME` " +
                    "    FROM" +
                    "        szyd_company  sc " +
                    "    LEFT JOIN" +
                    "        cof_org co " +
                    "            ON co.`CODE` = sc.org_code " +
                    "    WHERE" +
                    "        sc.decision_flag = 0 " +
                    "        AND sc.item_flag = 0  AND sc.org_code  IN ( ");
            try {
                returnOrgCode(orgCode1,i);
            }catch (RuntimeException e){}finally {
                sql.append(returnStringCode());
                sql.append(" ) ");
                if (!StringUtils.isEmpty(year)){
                    sql.append(" AND spb.created_time like '%"+year+"%'");
                }
                sql2.append(returnStringCode());
                sql2.append(" ) ");
                psuCountQuery = entityManager.createNativeQuery(sql.toString());
                List<String> alreadyPushList = returnResult(psuCountQuery.getResultList());
                psuCountQuery = entityManager.createNativeQuery(sql2.toString());
                List<String> needPushList = returnResult(psuCountQuery.getResultList());
                Map<String,String> map2 = new HashMap<>();
                totalCom+=needPushList.size();
                totalPushCom+=alreadyPushList.size();
                map2.put("已上报",alreadyPushList.size()+"家");
                map2.put("需要上报",needPushList.size()+"家");
                needPushList.removeAll(alreadyPushList);
                map2.put("未上报：",needPushList.size()+"家 "+convertListToString(needPushList)+"");
                map.put(String.valueOf(i),map2);
            }
        }
        Map<String,String> map1 = new HashMap<>();
        map1.put("已上报","1家");
        map1.put("需要上报","1家");
        map.put("1",map1);

        Map<String,String> map3 = new HashMap<>();
        map3.put("需要上报",totalCom+"家");
        map3.put("已上报",totalPushCom+"家");
        map.put("All",map3);
        return map;
    }
    //递归查询单位
    public  void returnOrgCode(List<String> paraneCodesl,Integer level){
        if (level != 2) {
            returnCode = cofOrgRepository.findByParentOrgCodeIn(paraneCodesl).stream().map(CofOrg::getCode).collect(Collectors.toList());
            level = level-1;
            returnOrgCode(returnCode,level);
        }else {
            returnCode = paraneCodesl;
        }
        if (returnCode != null && returnCode.size() != 0){
            throw new StopMsgException();
        }
    }
    //拼接字符串
    public String returnStringCode(){
        String sql  = "";
        for (int j = 0;j<returnCode.size();j++){
            sql+=(j == returnCode.size()-1)? "'"+returnCode.get(j)+"'":"'"+returnCode.get(j)+"',";
        }
        return sql;
    }
    //
    public List<String>  returnResult(List list){
        List<String> mapArrayList = new ArrayList<>();
        for (Object object :list){
            Object[] cells = (Object[]) object;
            mapArrayList.add(cells[1].toString());
        }
        return mapArrayList;
    }

    static class StopMsgException extends RuntimeException {
    }

    public static String convertListToString(List<String> strList) {
        StringBuffer sb = new StringBuffer();
        if (!CollectionUtils.isEmpty(strList)) {
            for (int i = 0; i < strList.size(); i++) {
                if (i == 0) {
                    sb.append("'").append(strList.get(i)).append("'");
                } else {
                    sb.append("、").append("'").append(strList.get(i)).append("'");
                }
            }
            return sb.toString();
        }else {
            return " ";
        }

    }
}
