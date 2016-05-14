package com.example.sandbox;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by glebl_000 on 5/14/2016.
 */
public class PullThread implements Runnable{
    private LinkedBlockingQueue<Task> pull;
    private Map<Integer, SharingFile> filesList;
    private Map<Integer, User> usersList;

    public PullThread(LinkedBlockingQueue<Task> pull, Map<Integer, SharingFile> filesList, Map<Integer, User> usersList){
        this.pull = pull;
        this.filesList = filesList;
        this.usersList = usersList;
    }

    @Override
    public void run() {
        while (true){
            try{
                Task currentTask = pull.take();
                switch(currentTask.requestType) {
                    case GET:
                        Task task = new Task(currentTask.getGlobalId(), currentTask.getName(), currentTask.getMacAddress(),
                                filesList.get(currentTask.getGlobalId()), request.SEND);
                        //send to socetbrea;
                        break;
                    case SEND:
                        filesList.put(currentTask.getGlobalId(), currentTask.getFile());
                        //todo
                        break;
                    case SEND_INFO:
                        break;
                    case GET_INFO:
                        break;


                }

            }catch (InterruptedException e){
                System.err.println("problems with task");
            }
        }
    }
}
