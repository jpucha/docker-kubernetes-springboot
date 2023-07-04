package org.jpucha.springcloud.mscv.cursos.clients;

import org.jpucha.springcloud.mscv.cursos.models.UsuarioModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url="localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    UsuarioModel detalle(@PathVariable Long id);

    @PostMapping("/")
    UsuarioModel crear(@RequestBody UsuarioModel usuarioModel);

    @GetMapping("usuarios-por-curso")
    List<UsuarioModel> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}