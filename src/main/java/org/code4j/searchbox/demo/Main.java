package org.code4j.searchbox.demo;

import org.code4j.searchbox.api.Index;
import org.code4j.searchbox.conf.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
public class Main {

    public static void main(String[] args) {
        testBulkIndex();
    }

    public static void testIndex(){
        AppConfig.init(Main.class.getResource("/app.properties").getPath());
        Index<Member> index = new Index<>();
        Member member = new Member();
        member.setmId("10001");
        member.setAbout("测试插入文档");
        member.setAge(25);
        member.setName("邢天宇");
        index.index(Member.INDEX,member);
    }

    public static void testBulkIndex(){
        AppConfig.init(Main.class.getResource("/app.properties").getPath());
        List<Member> members = new ArrayList<>();
        for (int index=1;index<1000;index++){
            Member member = new Member();
            member.setmId("1000"+index);
            member.setAbout("测试批量插入文档");
            member.setAge(25);
            member.setName("邢天宇");
            members.add(member);
        }
        Index<Member> index = new Index<>();
        index.bulkIndex(Member.INDEX,members);
    }
}
