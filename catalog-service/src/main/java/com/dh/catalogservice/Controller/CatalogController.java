package com.dh.catalogservice.Controller;

import com.dh.catalogservice.Model.CatalogDto;
import com.dh.catalogservice.Service.ICatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
	@Value("${server.port}")
	private String port;

	private ICatalogService catalogService;

	@Autowired
	public CatalogController(ICatalogService catalogService) {
		this.catalogService = catalogService;
	}

	@GetMapping("/db/{genre}")
	ResponseEntity<CatalogDto> getCatalogByGenreDB(@PathVariable String genre, HttpServletResponse response)
	{
		CatalogDto catalogDto = catalogService.getCatalogByGenreDB(genre);
		response.addHeader("port", port);
		return Objects.isNull(catalogDto)
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(catalogDto, HttpStatus.OK);
	}

	@GetMapping("/{genre}")
	ResponseEntity<CatalogDto> getCatalogByGenre(@PathVariable String genre, HttpServletResponse response)
	{
		CatalogDto catalogDTO = catalogService.getCatalogByGenreFeign(genre);
		response.addHeader("port", port);
		return Objects.isNull(catalogDTO)
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(catalogDTO, HttpStatus.OK);
	}

	//CircuitBreaker Test
	@GetMapping("/errors/{genre}")
	ResponseEntity<CatalogDto> getCatalogByGenreError(@PathVariable String genre, @RequestParam("movieErrors") Boolean movieErrors, @RequestParam("serieErrors") Boolean serieErrors, HttpServletResponse response) {
		CatalogDto catalogDto = catalogService.getCatalogByGenreFeignError(genre,movieErrors,serieErrors);
		response.addHeader("port", port);
		return Objects.isNull(catalogDto)
				? new ResponseEntity<>(HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(catalogDto, HttpStatus.OK);
	}
}
