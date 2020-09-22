package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class KravzivCryptoBot extends TelegramLongPollingBot {

    public ArrayList<Long> chatIds = new ArrayList<Long>();
    public Timer timer = new Timer();
    public Boolean timerOn = false, text_setted = false;
    int kl = 0;

     @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Welcome! You are logged in. Wait for notifications");

        if(!chatIds.contains(update.getMessage().getChat().getId())) {
            chatIds.add(update.getMessage().getChat().getId());
        }
        if(!timerOn){
            timerOn = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    text_setted = Volume_calculation.Volume_cal(message);
                    if(text_setted){
                        text_setted = false;
                        for(int i =0; i<chatIds.size(); i++){
                            message.setChatId(chatIds.get(i));
                            try {
                                execute(message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    kl++;
                    if(kl > 3*60){
                        message.setText("IT'S WORK !!!");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        kl = 0;
                    }
                }
            }, 0, 1*1000);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
     }

    @Override
    public String getBotUsername() {
        return "Kravziv_Crypto_v1_02_bot";
    }

    @Override
    public String getBotToken() {
        return "1377586865:AAFBrgy15GhYqweIIKHWhF_tkiSWe5aZ9_o";
    }
}
