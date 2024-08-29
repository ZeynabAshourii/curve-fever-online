package src.Message;

import java.io.Serializable;
public class MyString implements Serializable {
    public  Integer id;
    public String username;
    public MyString(Integer integer , String username){
        this.id = integer;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }
}
