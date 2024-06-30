package tihonel.com.github.workpermit.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class DtoPages {
    public List<Integer> pages;
    public int currentPage;

    public DtoPages(long quantity, int currentPage, int workPermitsForPage){
        double totalPages = Math.ceil((double) quantity / workPermitsForPage);

        if(currentPage > totalPages){
            currentPage = (int) totalPages;
        }

        pages = new ArrayList<>();
        //pagesBeforeCurrentPage
        int pageBeforeCurrentPage;
        if(currentPage <= 4){
            pageBeforeCurrentPage = 0;
        } else {
            pageBeforeCurrentPage = currentPage - 4;
        }
        while (pageBeforeCurrentPage < currentPage){
            pages.add(pageBeforeCurrentPage);
            pageBeforeCurrentPage++;
        }
        //currentPage
        pages.add(currentPage);
        //pagesAfterCurrentPage
        int pageAfterCurrentPage = currentPage + 1;
        while(pageAfterCurrentPage < totalPages && pageAfterCurrentPage < (currentPage + 5)){
            pages.add(pageAfterCurrentPage);
            pageAfterCurrentPage++;
        }

        this.currentPage = currentPage;
    }
}
