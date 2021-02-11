package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class StringTransformForkJoinExample extends RecursiveTask<List<String>> {

    private List<String> inputList;

    public StringTransformForkJoinExample(List<String> inputList) {
        this.inputList = inputList;
    }

    public static void main(String[] args) {

        stopWatch.start();
        List<String> resultList = new ArrayList<>();
        List<String> names = DataSet.namesList();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        StringTransformForkJoinExample stringTransformForkJoinExample = new StringTransformForkJoinExample(names);
        resultList = forkJoinPool.invoke(stringTransformForkJoinExample);

        stopWatch.stop();
        log("Final Result : "+ resultList);
        log("Total Time Taken : "+ stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    @Override
    protected List<String> compute() {
        if(inputList.size() <= 1) {
            List<String> result = new ArrayList<>();
            inputList.forEach(name -> result.add(addNameLengthTransform(name)));
            return result;
        }

        int midpoint = inputList.size()/2;
        ForkJoinTask<List<String>> leftInputList = new StringTransformForkJoinExample(inputList.subList(0, midpoint)).fork();
        inputList = inputList.subList(midpoint, inputList.size());
        List<String> rightResult = compute(); //recursivo
        List<String> leftResult = leftInputList.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
