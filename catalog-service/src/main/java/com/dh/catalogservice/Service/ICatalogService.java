package com.dh.catalogservice.Service;

import com.dh.catalogservice.Model.CatalogDto;

public interface ICatalogService {
    void save(CatalogDto catalogDto);
    CatalogDto getCatalogByGenreDB(String genre);
    CatalogDto getCatalogByGenreFeign(String genre);
    CatalogDto getCatalogByGenreFeignError(String genre, Boolean movieErrors, Boolean serieErrors);
}
