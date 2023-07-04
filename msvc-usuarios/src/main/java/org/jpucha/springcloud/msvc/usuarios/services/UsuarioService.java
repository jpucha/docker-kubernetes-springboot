package org.jpucha.springcloud.msvc.usuarios.services;

import org.jpucha.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();
    Optional <Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar (Long id);
    Optional<Usuario> getEmail(String email);

    boolean existePorEmail(String email);

    /**
     * Listar los detalles de usuarios dados sus ids
     * @param ids
     * @return
     */
    List<Usuario> listarPorIds (Iterable<Long> ids);
}
