package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class GradeProvider {
    public String listSql(@Param("sc_code")String sc_code) throws Exception {
        String sql = "select gr.*, sch.name as schoolName from grade gr left join school sch on sch.sc_code = gr.sc_code order by orderNumber";
        if(sc_code!=null&&sc_code.length()!=0){
            sql = "select gr.*, sch.name as schoolName from grade gr left join school sch on sch.sc_code = gr.sc_code where gr.sc_code = #{sc_code} order by orderNumber";
        }
        return String.format(sql);
    }

}
