package com.gr.imclient.command;

import java.util.Scanner;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 14:22
 */
public interface BaseCommand {

    void exec(Scanner scanner);

    String getKey();

    String getTip();

}
