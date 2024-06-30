package tihonel.com.github.workpermit.models;

public enum Role {
    RESPONSIBLE("responsible"),
    ADMITTING("admitting"),
    PRODUCER("producer"),
    OBSERVER("observer"),
    ISSUING("issuing"),
    MEMBER("member"),
    DRIVER("driver");

    final String value;

    private Role(String value){
        this.value = value;
    }
}
