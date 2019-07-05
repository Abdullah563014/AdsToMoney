package md.hasibul.islam.dhaka.adstomoney;

public class ModelClass {
    private String email;
    private String phone;
    private String userId;
    private String rewardDate;
    private String points;


    public ModelClass(){

    }

    public ModelClass(String email, String phone, String userId, String rewardDate, String points) {
        this.email = email;
        this.phone = phone;
        this.userId = userId;
        this.rewardDate = rewardDate;
        this.points = points;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(String rewardDate) {
        this.rewardDate = rewardDate;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
