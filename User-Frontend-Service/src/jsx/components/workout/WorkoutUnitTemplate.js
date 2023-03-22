import React, {useState} from 'react'
import {
   Col,
   Card,
   Table,
} from "react-bootstrap";
import { Link } from "react-router-dom";


export default function WorkoutUnitTemplate({trainingUnitPart, updateCurrentSession}) {

    const [repetitions, setRepetitions] = useState([])

    const [trainingData, setTrainingData] = useState([]);

const handleRepetitionsChange = (item, value) => {
  const newTrainingData = [...trainingData];
  const index = newTrainingData.findIndex((data) => data.seriesNumber === item.seriesNumber);
  if (index !== -1) {
    newTrainingData[index] = {
      ...newTrainingData[index],
      performedRepetitionsNumber: value,
    };
    setTrainingData(newTrainingData);
  } else {
    setTrainingData([
      ...newTrainingData,
      {
        seriesNumber: item.seriesNumber,
        targetSeriesRepetitionsNumber: item.repetitionsNumber,
        performedRepetitionsNumber: value,
        usedWeight: null,
      },
    ]);
  }
  updateCurrentSession(trainingUnitPart.unitPartId, trainingData)
};

const handleWeightChanged = (item, value) => {
  const newTrainingData = [...trainingData];
  const index = newTrainingData.findIndex((data) => data.seriesNumber === item.seriesNumber);
  if (index !== -1) {
    newTrainingData[index] = {
      ...newTrainingData[index],
      usedWeight: value,
    };
    setTrainingData(newTrainingData);
  } else {
    setTrainingData([
      ...newTrainingData,
      {
        seriesNumber: item.seriesNumber,
        targetSeriesRepetitionsNumber: item.repetitionsNumber,
        performedRepetitionsNumber: null,
        usedWeight: value,
      },
    ]);
  }

  updateCurrentSession(trainingUnitPart.unitPartId, trainingData)
};

const findWeightIndex = (item) => {
   let index = trainingData.findIndex(x => x.seriesNumber === item.seriesNumber)
   if (index != -1){
   return trainingData[index].usedWeight;
   } else {
       return ""
   }
}

const findRepetitionsIndex = (item) => {
            let index = trainingData.findIndex(x => x.seriesNumber === item.seriesNumber)
            if (index != -1){
            return trainingData[index].performedRepetitionsNumber;
            } else {
                return ""
            }
    }



   return (             
    <Col lg={12}>
     <Card>
    <Card.Header>
       <Card.Title>Ćwiczenie - {trainingUnitPart.exerciseName}</Card.Title>
    </Card.Header>
    <Card.Body> 

       <Table responsive>
          <thead>
             <tr>
                <th>
                   <strong>Numer serii</strong>
                </th>
                <th>
                   <strong>Założona liczba powtórzeń</strong>
                </th>
                <th>
                   <strong>Liczba powtórzeń z ostatniego treningu</strong>
                </th>
                <th>
                   <strong>Wykorzystany ciężar/obciążenie z ostatniego treningu</strong>
                </th>
                <th>
                   <strong>Dzisiejsza liczba powtórzeń treningu</strong>
                </th>
                <th>
                   <strong>Dzisiejszy wykorzystany ciężar/obciązenie</strong>
                </th>
             </tr>
          </thead>
          <tbody>
            {
                trainingUnitPart.targetSeriesRepetitionsDetails.map((item, index) => {
                    return (
                        <tr key={item.seriesNumber}>
                            <td>{item.seriesNumber}</td>
                            <td>{item.repetitionsNumber}</td>
                            <td>{item.pastWorkoutRepetitionsNumber}</td>
                            <td>{item.pastWorkoutUsedWeight}</td>
                            <td>                                    <input
                                    value = {findRepetitionsIndex(item)}
                                     type="text"
                                     name="numberOfCurrentRepetitions"
                                    className="form-control"
                                    placeholder="Wprowadz dzisiejszą liczbę powtórzeń"
                                    required
                                    onChange={(e) => handleRepetitionsChange(item, e.target.value)}
                                  //  style={{ backgroundColor: 'green'}}
                                 />
                                 </td>
                                 <td>                                
                                    <input
                                    value = {findWeightIndex(item)}
                                     type="text"
                                     name="numberOfCurrentWeight"
                                    className="form-control"
                                    placeholder="Wprowadz dzisiejszy ciężar"
                                    required
                                    onChange={(e) => handleWeightChanged(item, e.target.value)}
                                  //  style={{ backgroundColor: 'green'}}
                                 />
                                 </td>
                        </tr>
                    )

                })
            } 
          </tbody>
       </Table>
    </Card.Body>
 </Card>
 </Col>
   )
 }
 