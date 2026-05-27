package com.example.SmartHouse.mapper;

import java.util.stream.Collectors;

import com.example.SmartHouse.dto.UserDto;
import com.example.SmartHouse.dto.UserLoggedDto;
import com.example.SmartHouse.entity.Permission;
import com.example.SmartHouse.entity.User;

public class UserMapper {
    public static UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().getAuthority(),
                user.getRole().getPermissions().stream()
                .map(Permission::getAuthority)
                .collect(Collectors.toSet())
        );
    }
    public static User userDtoToUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        return user;
    }
    public static UserLoggedDto userToUserLoggedDto(User user) {
        return new UserLoggedDto(
                user.getUsername(),
                user.getRole().getAuthority(),
                user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet())
        );
    }
}