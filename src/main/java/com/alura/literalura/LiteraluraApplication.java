package com.alura.literalura;

import com.alura.literalura.controller.Index;
import com.alura.literalura.service.ILibroServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final ILibroServicio libroServicio;
	public LiteraluraApplication(ILibroServicio libroServicio){
		this.libroServicio=libroServicio;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Index index = new Index(libroServicio);
		index.mostrarMenu();
	}
}
