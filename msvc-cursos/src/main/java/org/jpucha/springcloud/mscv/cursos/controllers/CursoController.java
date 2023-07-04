package org.jpucha.springcloud.mscv.cursos.controllers;

import feign.FeignException;
import org.jpucha.springcloud.mscv.cursos.models.UsuarioModel;
import org.jpucha.springcloud.mscv.cursos.models.entity.Curso;
import org.jpucha.springcloud.mscv.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle (@PathVariable Long id){
        Optional<Curso> o = service.porIdConUsuarios(id);//service.porId(id);
        if(o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear (@Valid @RequestBody Curso curso, BindingResult result){
        //BindingResult aqui se incluye el resultado de la validacion con todos los mensages
        if(result.hasErrors()){
            return validar(result);
        }
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        //BindingResult aqui se incluye el resultado de la validacion con todos los mensages
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Curso> o = service.porId(id);
        if(o.isPresent()){
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody UsuarioModel usuarioModel, @PathVariable Long cursoId){
        Optional<UsuarioModel> o;
        try {
            o = service.asignarUsuario(usuarioModel, cursoId);

        }catch (FeignException.FeignClientException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicacion" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioModel usuarioModel, @PathVariable Long cursoId){
        Optional<UsuarioModel> o;
        try {
            o = service.crearUsuario(usuarioModel, cursoId);

        }catch (FeignException.FeignClientException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "No existe el usuario por" +
                            "el id o error en la comunicacion" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody UsuarioModel usuarioModel, @PathVariable Long cursoId){
        Optional<UsuarioModel> o;
        try {
            o = service.eliminarUsuario(usuarioModel, cursoId);

        }catch (FeignException.FeignClientException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "No existe el usuario por" +
                            "el id o error en la comunicacion" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarUsuarioPorCursoId(@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        //para mostrar el nombre del campo con su respectivo mensaje
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo "+ err.getField()+ " " +err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
