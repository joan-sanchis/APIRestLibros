package com.scalian.ArquitecturaSpringBoot;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.scalian.ArquitecturaSpringBoot.model.entity.Libro;
import com.scalian.ArquitecturaSpringBoot.repository.LibroRepository;

@SpringBootTest
class ArquitecturaSpringBootApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void initCommandLineRunnerGuardaTresLibros() throws Exception {
		LibroRepository libroRepository = Mockito.mock(LibroRepository.class);
		ArquitecturaSpringBootApplication application = new ArquitecturaSpringBootApplication();

		application.init(libroRepository).run();

		ArgumentCaptor<Libro> captor = ArgumentCaptor.forClass(Libro.class);
		verify(libroRepository, times(3)).save(captor.capture());
	}

	@Test
	void mainInvocaSpringApplicationRun() {
		try (MockedStatic<SpringApplication> springApplication = Mockito.mockStatic(SpringApplication.class)) {
			ArquitecturaSpringBootApplication.main(new String[] {});
			springApplication.verify(() -> SpringApplication.run(ArquitecturaSpringBootApplication.class, new String[] {}));
		}
	}

}

