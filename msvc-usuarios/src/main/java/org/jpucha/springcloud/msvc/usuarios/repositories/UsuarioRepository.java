package org.jpucha.springcloud.msvc.usuarios.repositories;

import org.jpucha.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    //spring jpa nos provee una declarativa mediante el nombre del metodo con palabras clave
    Optional<Usuario> findByEmail(String email);

    //consulta de jpa de hibernate personalizada con la notacion query
    @Query("select u from Usuario u where u.email=?1")
    Optional<Usuario> buscarPorCorreoElectronico(String email);

    boolean existsByEmail(String email);
}
