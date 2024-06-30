package tihonel.com.github.workpermit.dto;

import tihonel.com.github.workpermit.models.Technic;

import java.util.List;

public class DtoTechnicsList {
    List<Technic> list;

    public DtoTechnicsList(List<Technic> list) {
        this.list = list;
    }

    public DtoTechnicsList() {
    }

    public List<Technic> getList() {
        return list;
    }

    public void setList(List<Technic> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Dto{" +
                "list=" + list +
                '}';
    }
}
