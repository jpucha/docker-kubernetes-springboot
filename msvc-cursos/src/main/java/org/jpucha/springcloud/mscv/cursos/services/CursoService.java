package org.jpucha.springcloud.mscv.cursos.services;

import org.jpucha.springcloud.mscv.cursos.models.UsuarioModel;
import org.jpucha.springcloud.mscv.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();
    Optional<Curso> porId (Long Id);
    Curso guardar(Curso curso);
    void eliminar (Long id);

    /**
     * Elimina un usuario del curso asignado.
     * @param id
     */
    void eliminarCursoUsuarioPorId (Long id);

    /**
     * Asigna un usuario existente a un curso
     * @param usuarioModel datos del usuario
     * @param cursoId id del curso
     * @return
     */
    Optional<UsuarioModel> asignarUsuario(UsuarioModel usuarioModel, Long cursoId);

    Optional<Curso> porIdConUsuarios (Long Id);

    /**
     * Desde microservicio cursos le enviamos un nuevo usuario para que se inserte
     * en el microservicio usuarios y posteriormente le asigne a un curso existente
     * @param usuarioModel
     * @param cursoId
     * @return
     */
    Optional<UsuarioModel> crearUsuario(UsuarioModel usuarioModel, Long cursoId);

    /**
     * Eliminar usuario del curso, pero no es que se elimine de la base el usuario, sino
     * que en otras palabras se desasignara el usaurio de un curso y posteriormente se podria
     * asignar este usuario a otro curso.
     * @param usuarioModel
     * @param cursoId
     * @return
     */
    Optional<UsuarioModel> eliminarUsuario(UsuarioModel usuarioModel, Long cursoId);
}
