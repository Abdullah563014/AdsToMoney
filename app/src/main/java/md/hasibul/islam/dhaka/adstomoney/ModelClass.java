package md.hasibul.islam.dhaka.adstomoney;

public class ModelClass {
    private String email;
    private String phone;
    private String uId;


    public ModelClass(){

    }

    public ModelClass(String email, String phone, String uId) {
        this.email = email;
        this.phone = phone;
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
