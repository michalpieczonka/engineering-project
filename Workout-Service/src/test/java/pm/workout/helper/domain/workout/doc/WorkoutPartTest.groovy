package pm.workout.helper.domain.workout.doc

import spock.lang.Specification

class WorkoutPartTest extends Specification {
    def "should calculate valid volume"() {
        given:
        def series1 = new SeriesRepetitionsDetails(1, 10, 10, 60)
        def series2 = new SeriesRepetitionsDetails(2, 8, 10, 60)
        def series3 = new SeriesRepetitionsDetails(3, 6, 10, 60)
        def workoutPart = new WorkoutPart(1L, 1L, "Lift", List.of(series1, series2, series3))

        when:
        def result = workoutPart.calculateVolume()

        then:
        result == 1440

    }

    def "should calculate valid one rep max"() {
        given:
        def series1 = new SeriesRepetitionsDetails(1, 10, 10, 60)
        def series2 = new SeriesRepetitionsDetails(2, 8, 10, 60)
        def series3 = new SeriesRepetitionsDetails(3, 6, 10, 60)
        def workoutPart = new WorkoutPart(1L, 1L, "Lift", List.of(series1, series2, series3))

        when:
        def result = workoutPart.calculateOneRepMax()

        then:
        result == 224
    }

    def "should calculate valid  total reps"() {
        given:
        def series1 = new SeriesRepetitionsDetails(1, 10, 10, 60)
        def series2 = new SeriesRepetitionsDetails(2, 8, 10, 60)
        def series3 = new SeriesRepetitionsDetails(3, 6, 10, 60)
        def workoutPart = new WorkoutPart(1L, 1L, "Lift", List.of(series1, series2, series3))

        when:
        def result = workoutPart.calculateTotalRepetitions()

        then:
        result == 24
    }

    def "should calculate valid total series"() {
        given:
        def series1 = new SeriesRepetitionsDetails(1, 10, 10, 60)
        def series2 = new SeriesRepetitionsDetails(2, 8, 10, 60)
        def series3 = new SeriesRepetitionsDetails(3, 6, 10, 60)
        def workoutPart = new WorkoutPart(1L, 1L, "Lift", List.of(series1, series2, series3))

        when:
        def result = workoutPart.calculateTotalSeries()

        then:
        result == 3
    }
}
