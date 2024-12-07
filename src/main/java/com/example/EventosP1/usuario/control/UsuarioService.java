package com.example.EventosP1.usuario.control;

import com.example.EventosP1.usuario.model.*;
import com.example.EventosP1.utils.EmailSender;
import com.example.EventosP1.utils.Message;
import com.example.EventosP1.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class); // Logger

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    //CONSULTAR USUARIOS
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        logger.info("Iniciando búsqueda de todos los usuarios.");
        List<Usuario> usuarios = usuarioRepository.findAll();
        logger.info("Se encontraron {} usuarios.", usuarios.size());
        return new ResponseEntity<>(new Message(usuarios, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(long id) {
        logger.info("Iniciando búsqueda de usuario en concreto.");
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return new ResponseEntity<>(new Message(usuario, "Usuario encontrado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //CREAR UN USUARIO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(UsuarioDTO dto) {
        // Validar si ya existe un usuario con el mismo correo electrónico
        if (usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            return new ResponseEntity<>(new Message("El correo electrónico ya está en uso", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        // Validación de longitud de los campos
        if(dto.getNombre().length() > 50) {
            logger.warn("El nombre del usuario excede los 50 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getApellido().length() > 80) {
            logger.warn("El apellido del usuario excede los 80 caracteres.");
            return new ResponseEntity<>(new Message("El apellido excede el numero de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getCorreoElectronico().length() > 74) {
            logger.warn("El correo del usuario excede los 74 caracteres.");
            return new ResponseEntity<>(new Message("El correo excede el numero de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getContrasena().length() > 255) {
            logger.warn("La contraseña del usuario excede los 255 caracteres.");
            return new ResponseEntity<>(new Message("La contrasena excede el numero de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getContrasena().length() < 8) {
            logger.warn("La contraseña debe tener por lo menos 8 caracteres");
            return new ResponseEntity<>(new Message("La contraseña debe tener por lo menos 8 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (String.valueOf(dto.getTelefono()).length() != 10) {
            logger.warn("El teléfono del usuario no tiene exactamente 10 dígitos.");
            return new ResponseEntity<>(new Message("El teléfono debe tener exactamente 10 dígitos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        if( dto.getNombre().isBlank()){
            return new ResponseEntity<>(new Message("El nombre no puede estar vacio", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }


        // Validar si el teléfono ya existe
        if (usuarioRepository.existsByTelefono(dto.getTelefono())) {
            return new ResponseEntity<>(new Message("El teléfono ya está en uso", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        // Encripta la contraseña antes de guardar
        String encodedPassword = passwordEncoder.encode(dto.getContrasena());

        // Crear y guardar el usuario
        Usuario usuario = new Usuario(dto.getNombre(), dto.getApellido(), dto.getCorreoElectronico(), dto.getTelefono(), encodedPassword, Rol.ADMIN);
        usuario = usuarioRepository.saveAndFlush(usuario);

        return new ResponseEntity<>(new Message(usuario, "Registro exitoso", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    //ACTUALIZAR UN USUARIO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(PerfilDTO dto) {
        logger.info("Iniciando actualización de usuario con ID: {}", dto.getId());

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            logger.warn("Usuario con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();


        if (!usuario.getCorreoElectronico().equals(dto.getCorreoElectronico())) {
            if (usuarioRepository.existsByCorreoElectronicoAndIdNot(dto.getCorreoElectronico(), dto.getId())) {
                logger.warn("El correo electrónico {} ya está en uso por otro usuario.", dto.getCorreoElectronico());
                return new ResponseEntity<>(new Message("El correo electrónico ya está en uso por otro usuario", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }



        // Validación de longitud de los campos
        if(dto.getNombre().length() > 50) {
            logger.warn("El nombre del usuario excede los 50 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getApellido().length() > 80) {
            logger.warn("El apellido del usuario excede los 80 caracteres.");
            return new ResponseEntity<>(new Message("El apellido excede el numero de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(dto.getCorreoElectronico().length() > 74) {
            logger.warn("El correo del usuario excede los 74 caracteres.");
            return new ResponseEntity<>(new Message("El correo excede el numero de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (String.valueOf(dto.getTelefono()).length() != 10) {
            logger.warn("El teléfono del usuario no tiene exactamente 10 dígitos.");
            return new ResponseEntity<>(new Message("El teléfono debe tener exactamente 10 dígitos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }


        // Actualizar la información del usuario
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setTelefono(dto.getTelefono());

        usuario = usuarioRepository.saveAndFlush(usuario);
        logger.info("Usuario con ID {} actualizado exitosamente.", usuario.getId());

        if (usuario == null) {
            logger.error("La actualización del usuario con ID {} no se completó.", dto.getId());
            return new ResponseEntity<>(new Message("La actualización no se completó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Message(usuario, "Actualización exitosa", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CAMBIAR ESTADO DE USUARIO
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(UsuarioDTO dto) {
        logger.info("Iniciando cambio de estado de usuario con ID: {}", dto.getId());

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            logger.warn("Usuario con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setStatus(!usuario.isStatus());
        usuarioRepository.saveAndFlush(usuario);
        logger.info("Estado del usuario con ID {} actualizado a {}.", usuario.getId(), usuario.isStatus());

        return new ResponseEntity<>(new Message(usuario, "Estado actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //EDITAR PERFIL (SÓLO PARA ADMIN)
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> updateProfile(PerfilDTO dto) {
        logger.info("Iniciando actualización de perfil para el usuario con ID: {}", dto.getId());

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            logger.warn("Usuario con ID {} no encontrado.", dto.getId());
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        // Verificar si el nuevo correo ya está en uso por otro usuario
        if (!usuario.getCorreoElectronico().equals(dto.getCorreoElectronico()) &&
                usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            logger.warn("El correo electrónico {} ya está en uso por otro usuario.", dto.getCorreoElectronico());
            return new ResponseEntity<>(new Message("El correo electrónico ya está en uso por otro usuario", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Validar los campos permitidos para la actualización del perfil
        if (dto.getNombre().length() > 100) {
            logger.warn("El nombre del usuario excede los 100 caracteres.");
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getApellido().length() > 150) {
            logger.warn("El apellido del usuario excede los 150 caracteres.");
            return new ResponseEntity<>(new Message("El apellido excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getCorreoElectronico().length() > 150) {
            logger.warn("El correo del usuario excede los 150 caracteres.");
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (String.valueOf(dto.getTelefono()).length() != 10) {
            logger.warn("El teléfono del usuario no tiene exactamente 10 dígitos.");
            return new ResponseEntity<>(new Message("El teléfono debe tener exactamente 10 dígitos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Actualizar la información permitida del usuario
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setTelefono(dto.getTelefono());

        // Guardar los cambios
        usuario = usuarioRepository.saveAndFlush(usuario);
        logger.info("Perfil del usuario con ID {} actualizado exitosamente.", usuario.getId());

        return new ResponseEntity<>(new Message(usuario, "Perfil actualizado exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // CAMBIO DE CONTRASEÑA DESDE PERFIL (SÓLO PARA ADMIN)
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changePassword(Long userId, String newPassword) {
        logger.info("Iniciando cambio de contraseña para el usuario con ID: {}", userId);

        // Buscar al usuario por ID
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (!usuarioOptional.isPresent()) {
            logger.warn("Usuario con ID {} no encontrado.", userId);
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }



        // Actualizar la contraseña del usuario
        Usuario usuario = usuarioOptional.get();

        Rol adminRole = usuario.getRol();
        // Validar que el rol del usuario solicitante sea ADMIN
        if (adminRole != Rol.ADMIN) {
            logger.warn("Acceso denegado. Solo los administradores pueden cambiar la contraseña de otro usuario.");
            return new ResponseEntity<>(new Message("No tiene permisos para cambiar la contraseña", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }

        // Validar la longitud de la nueva contraseña
        if (newPassword == null || newPassword.isBlank()) {
            return new ResponseEntity<>(new Message("La contraseña no puede estar vacía", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (newPassword.length() > 255) {
            return new ResponseEntity<>(new Message("La contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Encripta la nueva contraseña antes de guardarla
        String encodedPassword = passwordEncoder.encode(newPassword);

        usuario.setContrasena(encodedPassword); // Aquí podrías aplicar un método de hashing si es necesario
        usuarioRepository.saveAndFlush(usuario);
        logger.info("Contraseña del usuario con ID {} actualizada exitosamente.", usuario.getId());

        return new ResponseEntity<>(new Message("Contraseña actualizada exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // RECUPERACIÓN DE LA CONTRASEÑA

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> sendEmail(String correo) {
        Optional<Usuario> optional = usuarioRepository.findFirstByCorreoElectronico(correo);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new Message("No hay un usuario registrado con ese correo", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        // Generar un código de 5 dígitos
        String code = String.format("%05d", new Random().nextInt(100000));

        Usuario usuarioObtenido = optional.get();
        usuarioObtenido.setCode(code); // Guardar el código en el usuario obtenido
        usuarioObtenido.setCodeExpiration(LocalDateTime.now().plusMinutes(10)); // Expiración en 10 minutos
        usuarioRepository.save(usuarioObtenido);


        // Enviar el correo
        emailSender.sendSimpleMessage(usuarioObtenido.getCorreoElectronico(),
                "Solicitud de restablecimiento de contraseña",
                "Tu código de verificación es: " + code);

        return new ResponseEntity<>( new Message(usuarioObtenido,"Correo enviado", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Message> verifyCode(String codigo) {
        Optional<Usuario> optional = usuarioRepository.findFirstByCode(codigo);

        if (!optional.isPresent()) {
            return new ResponseEntity<>(new Message("Código incorrecto o expirado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Usuario usuarioObtenido = optional.get();

        logger.info("El usuario que se quiere verificar es: " + usuarioObtenido.getNombre());

        System.out.println("El usuario que se quiere verificar es:" + usuarioObtenido.getNombre());

        // Comprobar si el código ha expirado
        if (usuarioObtenido.getCodeExpiration().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(new Message("Código incorrecto o expirado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Message(usuarioObtenido,"Código verificado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> updatePassword(String correo, String nuevaContrasena) {
        Optional<Usuario> optional = usuarioRepository.findFirstByCorreoElectronico(correo);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        logger.info("Si se encontro el usuario con el correo al cambiar la contra");
        Usuario usuario = optional.get();

        // Validar que la nueva contraseña cumple con los requisitos
        if (nuevaContrasena.length() < 8) {
            return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(nuevaContrasena.length() > 255) {
            return new ResponseEntity<>(new Message("La contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = passwordEncoder.encode(nuevaContrasena);
        usuario.setContrasena(encodedPassword);
        usuarioRepository.saveAndFlush(usuario);
        return new ResponseEntity<>(new Message("Contraseña actualizada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
