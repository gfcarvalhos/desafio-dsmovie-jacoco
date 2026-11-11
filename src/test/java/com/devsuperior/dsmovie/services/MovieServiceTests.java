package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

    @Mock
    private MovieRepository repository;

    private MovieEntity movie;
    private MovieDTO movieDTO;
    private String tittle;
    private Long existingId, nonExistingId, dependentId;

    @BeforeEach
    void setUp() throws Exception  {
        movie = MovieFactory.createMovieEntity();
        movieDTO = MovieFactory.createMovieDTO();

        tittle = movie.getTitle();
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

    }

    @Test
	public void findAllShouldReturnPagedMovieDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<MovieEntity> page = new PageImpl<>(List.of(movie));

        Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);

        Page<MovieDTO> result = service.findAll(tittle, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(tittle, result.iterator().next().getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));

        MovieDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(movie.getTitle(), result.getTitle());
        Assertions.assertEquals(movie.getCount(), result.getCount());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
        Mockito.when(repository.save(any())).thenReturn(movie);

        MovieDTO result = service.insert(movieDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movieDTO.getTitle(), result.getTitle());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
        Mockito.when(repository.getReferenceById(any())).thenReturn(movie);
        Mockito.when(repository.save(any())).thenReturn(movie);

        MovieDTO result = service.update(existingId, movieDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movieDTO.getTitle(), result.getTitle());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repository.getReferenceById(any())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, movieDTO);
        });
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
        Mockito.when(repository.existsById(any())).thenReturn(true);
        Mockito.doNothing().when(repository).deleteById(existingId);

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repository.existsById(any())).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Mockito.when(repository.existsById(any())).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
	}
}
