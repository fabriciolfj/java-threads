package com.learnjava.parallel;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamsExample {

    public List<String> stringTransform(final List<String> namesList) {
        return namesList
                .parallelStream()
                .map(this::addNameLenghtTransform)
                //.sequential() //cancela o paralelismo
                .collect(Collectors.toList());
    }

    public List<String> stringTransform_1(final List<String> namesList, final boolean isParallel) {

        final Stream<String> values = namesList.stream();

        if (isParallel) {
            values.parallel();
        }

        return values
                .map(this::addNameLenghtTransform)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        final List<String> namesList = DataSet.namesList();
        ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();
        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform(namesList);
        log("resultList: " + resultList);
        timeTaken();
    }

    private String addNameLenghtTransform(final String name) {
        delay(500);
        return name.length() + " - " + name;
    }
}
