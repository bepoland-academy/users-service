package pl.betse.beontime.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.bo.RoleDTO;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.model.custom_exceptions.EmptyRoleListException;
import pl.betse.beontime.model.custom_exceptions.RoleNotFoundException;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.utils.UserDTOListBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin("*")
public class RoleController {


    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public @ResponseBody
    List<RoleDTO> getRoleListWithUsers() {

        List<RoleDTO> roleList = new ArrayList<>();
        roleService.findAll().forEach(x -> {
            List<UserDTO> usersList = new ArrayList<>();
            for (UserEntity userEntity : x.getUserEntities()) {
                UserDTOListBuilder.build(usersList, userEntity);
            }
            roleList.add(RoleDTO.builder().role(x.getRole()).id(x.getId()).users(usersList).build());
        });

        if (roleList.isEmpty()) {
            throw new EmptyRoleListException();
        }

        return roleList;
    }


    @GetMapping("/{id}")
    public @ResponseBody
    RoleDTO getRolesByIdWithUsers(@PathVariable("id") String roleId) {

        if (!roleService.existsById(Integer.valueOf(roleId))) {
            throw new RoleNotFoundException();
        }

        RoleEntity roleEntity = roleService.findById(Integer.valueOf(roleId));
        List<UserDTO> usersList = new ArrayList<>();
        for (UserEntity userEntity : roleEntity.getUserEntities()) {
            UserDTOListBuilder.build(usersList, userEntity);
        }

        return RoleDTO.builder().role(roleEntity.getRole()).id(roleEntity.getId()).users(usersList).build();
    }
}
