package org.jpucha.springcloud.msvc.usuarios.controllers;

import org.jpucha.springcloud.msvc.usuarios.models.entity.Usuario;
import org.jpucha.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;


    @GetMapping
    public List<Usuario> listar () {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle (@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()){
            //return ResponseEntity.ok().body(usuarioOptional);
            return ResponseEntity.ok(usuarioOptional);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        //BindingResult aqui se incluye el resultado de la validacion con todos los mensajes
        if(result.hasErrors()){
            return validar(result);
        }

        if(!usuario.getEmail().isEmpty() && service.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje ","Ya existe un usuario con el correo electronico"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        //BindingResult aqui se incluye el resultado de la validacion con todos los mensages
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> o = service.porId(id);

        if(o.isPresent()){
            Usuario usuarioBd = o.get();
            if(!usuario.getEmail().isEmpty() &&
                    !usuario.getEmail().equalsIgnoreCase(usuarioBd.getEmail()) &&
                    service.getEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("Mensaje ","Ya existe un usuario con el correo electronico"));
            }
            usuarioBd.setNombre(usuario.getNombre());
            usuarioBd.setEmail(usuario.getEmail());
            usuarioBd.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioBd));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso (@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        Optional<Usuario> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        //para mostrar el nombre del ccampo con su respectivo mensaje
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo "+ err.getField()+ " " +err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
