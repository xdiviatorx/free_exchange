package com.technologies.mobile.free_exchange;

import java.util.Stack;

/**
 * Created by diviator on 27.12.2016.
 */

public class AppSingle {

    private static AppSingle instance = new AppSingle();

    private AppSingle(){}

    public static AppSingle getInstance(){
        if( instance == null ){
            instance = new AppSingle();
        }
        return instance;
    }

    private int currFragmentIndex = -1;
    private Stack<Integer> backStack = new Stack<>();

    public synchronized int getCurrFragmentIndex() {
        return currFragmentIndex;
    }

    public synchronized void setCurrFragmentIndex(int currFragmentIndex, boolean backStack) {
        if( backStack && this.currFragmentIndex != -1){
            this.backStack.push(this.currFragmentIndex);
        }else{
            this.backStack.clear();
        }
        this.currFragmentIndex = currFragmentIndex;
    }

    private synchronized int getPrevFragmentIndex(){
        if( !backStack.isEmpty() ) {
            return backStack.pop();
        }else{
            return -1;
        }
    }

    public synchronized void popBackStack(){
        this.currFragmentIndex = getPrevFragmentIndex();
    }
}
