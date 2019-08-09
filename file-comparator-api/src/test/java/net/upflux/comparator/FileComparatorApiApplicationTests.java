package net.upflux.comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import net.upflux.comparator.model.FileFrom;
import net.upflux.comparator.model.FileTo;
import net.upflux.comparator.repository.FilesFromRepository;
import net.upflux.comparator.repository.FilesToRepository;
import net.upflux.comparator.service.FileFromService;
import net.upflux.comparator.service.FileToService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileComparatorApiApplicationTests {
	
	@MockBean
	FilesFromRepository filesFromRepository;
	
	@MockBean
	FilesToRepository filesToRepository;

	@Autowired
	FileFromService fileFromService;

	@Autowired
	FileToService fileToService;
	
	FileFrom fileFrom, fileFrom1;
	FileTo fileTo, fileTo1, fileTo2, fileTo3;
	
	@Before
	public void initVariables() {
		
		fileTo = new FileTo("file.txt", "content content content");
		fileTo.setDate("2019071823452219");
		fileTo.setId("oiio32");
		
		fileTo1 = new FileTo("file1.txt", "content1 content1 content1");
		fileTo2 = new FileTo("file2.txt", "content1 content1 content1");
		fileTo3 = new FileTo("file3.txt", "content3 content3 content3");
		
		fileFrom = new FileFrom("file.txt", "content1 content1 content1");
		fileFrom1 = new FileFrom("file2.txt", "content1 content1 content1");

	}
	
	@Test
	public void createFileToTest() {
		when(filesToRepository.save(fileTo)).thenReturn(fileTo);
		assertEquals(fileTo, filesToRepository.save(fileTo));
	}
	
	@Test
	public void deleteFileToTest() {
		fileToService.deleteFile("file.txt");
		verify(filesToRepository, atLeast(1)).findByName("file.txt");
	}
	
	@Test
	public void equalsFileToTest() {
		assertFalse(fileTo.equals(fileTo1));
	}
	
	@Test
	public void equalsFilesFromToTest() {
		assertTrue(fileFrom1.getName().equals(fileTo2.getName()));
	}
	
	@Test
	public void getModelFileToTest() {
		assertEquals("file1.txt", fileTo1.getName());
		assertEquals("2019071823452219", fileTo.getDate());
		assertEquals("oiio32", fileTo.getId());
	}
	
	@Test
	public void readFilesFromTest() {
		when(filesFromRepository.findAll()).thenReturn(Stream.of(fileFrom, fileFrom1).collect(Collectors.toList()));
		assertEquals(2, fileFromService.readFiles().size());
	}
	
	@Test
	public void readFilesToTest() {
		when(filesToRepository.findAll()).thenReturn(Stream.of(fileTo1, fileTo2, fileTo3).collect(Collectors.toList()));
		assertEquals(3, fileToService.readFiles().size());
	}

}
