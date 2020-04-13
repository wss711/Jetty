package com.gr.imclient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 14:36
 */
@Data
@Service("LogoutConsoleCommand")
public class LogoutConsoleCommand implements BaseCommand {

    public static final String KEY = "10";

    @Override
    public void exec(Scanner scanner) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "退出";
    }
}
