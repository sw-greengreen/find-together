package com.hackathon.findtogether.controller;

import com.hackathon.findtogether.domain.User;
import com.hackathon.findtogether.domain.UserStatus;
import com.hackathon.findtogether.dto.request.FindPasswordDto;
import com.hackathon.findtogether.dto.request.FindUsernameDto;
import com.hackathon.findtogether.dto.request.LoginUserDto;
import com.hackathon.findtogether.dto.request.SignupUserDto;
import com.hackathon.findtogether.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/auth/signup")
    public Response createUser(@RequestBody @Valid SignupUserDto userDto) throws Exception {
        User user = User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .point(0)
                .achievement(UserStatus.BASIC)
                .build();
        userService.join(user);
        return new Response(201, true, "회원가입에 성공하였습니다.", userDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/login")
    public Response loginUser(@RequestBody @Valid LoginUserDto userDto) {
        User requestUser = userService.login(userDto);

        if (requestUser == null)
            return new Response(400, false, "로그인에 실패하였습니다. 해당 유저가 존재하지 않습니다.", userDto);
        return new Response(200, true, "로그인에 성공하였습니다.", userDto);
    }

    //아이디 찾기 -> 이름 전화번호 일치 -> 아이디 공개
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/find/username")
    public Response findUsername(@RequestBody @Valid FindUsernameDto findUsernameDto) {
        User requestUser = userService.findCheckUsername(findUsernameDto);

        if (requestUser == null)
            return new Response(400, false, "아이디를 찾는데 실패하였습니다.", findUsernameDto);
        else {
            String findUsername = requestUser.getUsername();
            return new Response(200, true, "아이디를 찾는데 성공하였습니다.", findUsername);
        }
    }

    //비밀번호 찾기 -> 아이디 전화번호 일치 -> 비번 일부 공개
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/find/password")
    public Response findUsername(@RequestBody @Valid FindPasswordDto findPasswordDto) {
        User requestUser = userService.findCheckPassword(findPasswordDto);

        if (requestUser == null)
            return new Response(400, false, "비밀번호를 찾는데 실패하였습니다.", findPasswordDto);
        else {
            String findPassword = requestUser.getPassword();
            int passwordLen = findPassword.length();
            String maskingPassword = findPassword.substring(0, passwordLen / 2);
            for (int i = 0; i < passwordLen - passwordLen / 2; i++)
                maskingPassword += "*";
            //String maskingPassword = findPassword.replaceAll("(?<=.{passwordLen}).", "x");
            return new Response(200, true, "비밀번호를 찾는데 성공하였습니다.", maskingPassword);
        }
    }
}