package com.twitter.challenge;

import com.twitter.challenge.viewModel.WeatherViewModel;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StdDevCalculationTests {
    @Test
    public void testStdDevCalculation() {

        WeatherViewModel weatherViewModel = new WeatherViewModel();

        ArrayList<Double> tempList = new ArrayList<>();
        tempList.add(16.83);
        tempList.add(11.15);
        tempList.add(14.2);
        tempList.add(9.88);
        tempList.add(19.19);
        assertThat(weatherViewModel.calculateStdDev(tempList)).isEqualTo(3.87);
    }
}