package net.puzzleco.gattapp.model;

public class IDModel {

    public static final String[] BLOOD_ARRAY = new String[]{"AB", "A+", "A-", "B+", "B-", "O+", "O-"};
    public static final String[] SEX_ARRAY = new String[]{"Male", "Female", "Else"};

    private String name;
    private int sex;
    private int blood;
    private int[] birth;
    private String height;
    private String weight;
    private boolean donor;
    private String emergency;
    private String conditions;
    private String notes;
    private String allergies;
    private String medications;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int[] getBirth() {
        return birth;
    }

    public void setBirth(int[] birth) {
        this.birth = birth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isDonor() {
        return donor;
    }

    public void setDonor(boolean donor) {
        this.donor = donor;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }
}
