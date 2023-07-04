package org.jpucha.springcloud.mscv.cursos.services;

import org.jpucha.springcloud.mscv.cursos.clients.UsuarioClientRest;
import org.jpucha.springcloud.mscv.cursos.models.UsuarioModel;
import org.jpucha.springcloud.mscv.cursos.models.entity.Curso;
import org.jpucha.springcloud.mscv.cursos.models.entity.CursoUsuario;
import org.jpucha.springcloud.mscv.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Transactional(readOnly = true)
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Elimina un usuario del curso asignado.
     *
     * @param id
     */
    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<UsuarioModel> asignarUsuario(UsuarioModel usuarioModel, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            UsuarioModel usuarioMsvc = client.detalle(usuarioModel.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCourseUser(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Curso curso = o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu -> cu.getUsuarioId()).
                        collect(Collectors.toList());
                List<UsuarioModel> usuarios = client.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UsuarioModel> crearUsuario(UsuarioModel usuarioModel, Long cursoId) {

        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            UsuarioModel usuarioNuevoMsvc = client.crear(usuarioModel);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCourseUser(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UsuarioModel> eliminarUsuario(UsuarioModel usuarioModel, Long cursoId) {

        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            UsuarioModel usuarioMsvc = client.detalle(usuarioModel.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCourseUser(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
