package org.jpucha.springcloud.mscv.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


//Intefaz parecida al crud repository.
// Habilitamos en nuestra aplicacion el contexto de feing y poder implementar
// nuestras api rest de forma declarativa
@EnableFeignClients
@SpringBootApplication
public class MsvcCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcCursosApplication.class, args);
	}

}
