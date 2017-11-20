package org.code4j.searchbox.demo;

import org.code4j.searchbox.annotation.Id;
import org.code4j.searchbox.annotation.Type;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
@Type("member")
public class Member {

    public static final String INDEX = "test_myindex";

    @Id
    private String mId;

    private String name;

    private Integer age;

    private String about;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
