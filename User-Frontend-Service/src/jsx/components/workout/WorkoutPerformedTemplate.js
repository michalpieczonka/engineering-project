import React, {useState, useEffect} from 'react'
import { useHistory, useParams} from "react-router-dom";
import { getTrainingPlan } from '../../../services/TrainingPlanService';
import swal from "sweetalert";
import { Button, Col, Card, Table,ProgressBar,Badge, Tab, Nav } from "react-bootstrap";
import { store } from "../../../store/store";
import {getWorkout} from '../../../services/WorkoutService';
import {mapTrainingDay} from '../../components/workout-create/CommonMappers'
import moment from 'moment';
import BarChartt from './BarChartt';

export default function WorkoutPerformedTemplate() {
   const params = useParams();
   const state = store.getState();

   let userId = state.auth.auth.userId;
   let workoutId = params.workoutId;

   const[isLoading, setIsLoading] = useState(true)
   const[workoutLoaded, setWorkoutLoaded] = useState(false)
   const[trainingPlan, setTrainingPlan] = useState(null)

   const[trainingMinutes, setTrainingMinutes] = useState(0)


   const [workout, setWorkout] = useState({})
//todo: naprawić zapisywanie treningu bo brakuje nazwy ćwiczenia oraz liczby powórzen z wczesniejszego treningu
   const date = moment();
   
   useEffect(() => { 
      getWorkout(workoutId).then((response) => {
         let tmp = response.data
         let workoutPlan = {
            workoutId: tmp.workoutId,
            trainingPlanId: tmp.trainingPlanId,
            trainingUnitId: tmp.trainingUnitId,
            startedAt: tmp.startedAt,
            finishedAt: tmp.finishedAt,
            workoutAssessment: {rate: tmp.workoutAssessment.personalRate, comment: tmp.workoutAssessment.additionalComment},
            trainingDay: mapTrainingDay(tmp.trainingDay),
            workoutParts: tmp.workoutParts.map(part => (
               {unitPartId: part.trainingUnitPartId, exerciseId: part.exerciseId, exerciseName: part.exerciseName,
               targetSeriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => (
                  {seriesNumber: s.seriesNumber, targetRepetitionsNumber: s.targetSeriesRepetitionsNumber, performedRepetitionsNumber: s.performedRepetitionsNumber, pastWorkoutUsedWeight: s.usedWeight})
                  ).sort((a,b) => { return a.seriesNumber - b.seriesNumber})
               }))

         }

        setWorkout(workoutPlan)
        setIsLoading(false)
        setWorkoutLoaded(true)
        const wStart = moment(workout.startedAt).format("DD.MM.YYYY HH:mm");
        const wFinish = moment(workout.finishedAt).format("DD.MM.YYYY HH:mm");

         })
      .catch(error => swal("Błąd", "Nie udało się załadować szczegółów jednostki treningowej", "error").then(() => history.push('/workout-center')))
     }, []); 

     useEffect(() => { 
      if(workoutLoaded){

         getTrainingPlan(workout.trainingPlanId).then((response) => {
            let plan = response.data
            let tmp = {
                id: plan.id,
                title:  plan.title,
                description: plan.planDescription,
                numberOfTrainingDays: plan.numberOfTrainingDays,
                trainingDays: plan.trainingDays,
                planPriority: plan.planPriority,
                planType: 'HYBRID', //todo: fix it
                preferredTrainingIntership: plan.preferredTrainingIntership,
                planCreatorUserId: plan.planCreatorUserId,
                planUsersIds: plan.planUsersIds,
                isPublic: plan.public,
                planRates: plan.planRates.map(x => ({id: x.id, description: x.description, rate: x.rate})),
                trainingUnits: plan.trainingUnits.map(unit => ({id: unit.id, trainingDay: unit.trainingDay, trainingUnitParts:
                    unit.trainingUnitParts.map(part => ({id: part.id, exerciseId: part.exerciseId, exerciseName: part.exerciseName, targetMuscle: part.targetMuscle,
                     additionalMuscles: part.additionalMuscles, //.map(m => mapMuscleGroup(m)), 
                seriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => ({seriesNumber: s.seriesNumber, repetitionsNumber: s.repetitionsNumber}))}))}))
            }
            setTrainingPlan(tmp)
    
            })
         .catch(error => swal("Błąd", "Nie udało się załadować szczegółów planu treningowego", "error"))

      }
     }, [workoutLoaded]); 


   const history = useHistory()

   const prepareRepetitionsChartData = () => {
    let sums = []
    workout.workoutParts.forEach((item) => {
      let sum = 0
      item.targetSeriesRepetitionsDetails.forEach((item) => {
          sum += item.performedRepetitionsNumber
      })
      sums.push(sum)
  })
  return sums;
   }

   const prepareSeriesChartData = () => {
    let sums = []
    workout.workoutParts.forEach((item) => {
      let sum = 0
      item.targetSeriesRepetitionsDetails.forEach((item) => {
          sum += item.seriesNumber
      })
      sums.push(sum)
  })
  return sums;
   }

   const prepareVolumeChartData = () => {
    let sums = []
    workout.workoutParts.forEach((item) => {
      let sum = 0
      item.targetSeriesRepetitionsDetails.forEach((item) => {
          sum += item.pastWorkoutUsedWeight
      })
      sums.push(sum)
  })
  return sums;
   }


    return !workout ? null : (
        <div>
       
            <div className="card">
                     <div className="card-header">
                  <h4 className="card-title">Szczegoły wykonanej jednostki treningowej </h4>
               </div>
                     <div className="card-body">
                        <div className="row">
                        <div className='col-xl-4 col-lg-12 col-sm-12'>
          <div className='card'>
            <div className='card-header border-0 pb-0'>
              <h2 className='card-title'>Szczegóły sesji treningowej</h2>
            </div>
            <div className='card-body pb-0'>
              <ul className='list-group list-group-flush'>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Plan treningowy:</strong>
                  {trainingPlan && <span className='mb-0'>{trainingPlan.title}</span>}
                  {!trainingPlan && <span className='mb-0'>Trwa ładowanie</span>}
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Dzień treningowy:</strong>
                  <span className='mb-0'>{workout.trainingDay}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Data rozpoczęcia:</strong>
                  <span className='mb-0'>{moment(workout.startedAt).format("DD.MM.YYYY HH:mm")}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Data zakończenia:</strong>
                  <span className='mb-0'>{moment(workout.finishedAt).format("DD.MM.YYYY HH:mm")}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Subiektywna ocena trudności:</strong>
                  <span className='mb-0'>{workout?.workoutAssessment?.rate}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Dodatkowy komentarz:</strong>
                  <span className='mb-0'>{workout?.workoutAssessment?.comment}</span>
                </li>
              </ul>
            </div>
            <div className='card-footer pt-0 pb-0 text-center'>
              <div className='row'>
                <div className='col-4 pt-3 pb-3 border-right'>
                  <h3 className='mb-1 text-primary'>{workout?.workoutParts?.reduce((acc, workoutPart) => acc + workoutPart.targetSeriesRepetitionsDetails.reduce((acc, series) => acc + series.performedRepetitionsNumber, 0), 0)}</h3>
                  <span>Powtórzeń łącznie</span>
                </div>
                <div className='col-4 pt-3 pb-3 border-right'>
                  <h3 className='mb-1 text-primary'>{workout?.workoutParts?.length}</h3>
                  <span>Ćwiczeń</span>
                </div>
                <div className='col-4 pt-3 pb-3'>
                  <h3 className='mb-1 text-primary'>{trainingMinutes}</h3>
                  <span>Minut wysiłku</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className='col-xl-8 col-xxl-8 col-lg-12 col-sm-12'>
          <div id='user-activity' className='card'>
            <Tab.Container defaultActiveKey='reps'>
              <div className='card-header border-0 pb-0 d-sm-flex d-block'>
                <h4 className='card-title'>Zestawienia</h4>
                <div className='card-action mb-sm-0 my-2'>
                  <Nav className='nav nav-tabs' role='tablist'>
                    <Nav.Item className='nav-item'>
                        <Nav.Link	className='' data-toggle='tab'  role='tab' eventKey='reps'>Liczba powtórzeń/Ćwiczenie</Nav.Link>
                    </Nav.Item>
                    <Nav.Item className=''>
                      <Nav.Link
                        className='nav-link'
                        data-toggle='tab'             
                        role='tab'
                        eventKey='series'>
                        Liczba serii/Ćwiczenie
                      </Nav.Link>
                    </Nav.Item>
                    <Nav.Item className=''>
                      <Nav.Link
                        className='nav-link'
                        data-toggle='tab'             
                        role='tab'
                        eventKey='volume'>
                        Tonaż/Ćwiczenie
                      </Nav.Link>
                    </Nav.Item>
                  </Nav>
                </div>
              </div>
              <div className='card-body'>
               
                <Tab.Content className='tab-content' id='myTabContent'>
                  <Tab.Pane eventKey='reps' id='user' role='tabpanel'>
                  {!isLoading &&  <BarChartt labelsArray={workout.workoutParts.map((item) => item.exerciseName)} dataArray = {prepareRepetitionsChartData()} labelMessage =  {'Łączna liczba wykonanych powtórzeń'}/>}
                  </Tab.Pane>
                  <Tab.Pane eventKey='series' id='user' role='tabpanel'>
                   {workoutLoaded &&  <BarChartt labelsArray={workout.workoutParts.map((item) => item.exerciseName)} dataArray = {prepareSeriesChartData()} labelMessage =  {'Łączna liczba wykonanych serii'}/>}
                  </Tab.Pane>
                 <Tab.Pane eventKey='volume' id='user' role='tabpanel'>
                 {workoutLoaded &&  <BarChartt labelsArray={workout.workoutParts.map((item) => item.exerciseName)} dataArray = {prepareVolumeChartData()} labelMessage =  {'Łączna objętość/tonaż '}/>}
                  </Tab.Pane> 
                </Tab.Content>
              </div>
            </Tab.Container>
          </div>
        </div>
                
    </div>
 </div>
 

</div>



           <div className="row">



           <div className="col-lg-12">
                 <div className="card">
                    <div className="card-header">
                       <h4 className="card-title">Wypełniony szkielet planu  treningowego</h4>
                       <div className="mt-4">
                        <center>
                     <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {history.replace(`/workout-center`)}}>
                               Zakończ przegląd
                        </Button> 
                        </center>
                        </div>

                       
                    </div>
                 

               <div className="card-body">
                     {!isLoading && workout.workoutParts.map((day, index) => {
                           return (             
                            <Col lg={12}>
                             <Card>
                            <Card.Header>
                               <Card.Title>Ćwiczenie - {day.exerciseName}</Card.Title>
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
                                           <strong>Liczba powtórzeń z wykonanego treningu</strong>
                                        </th>
                                        <th>
                                           <strong>Obciążenie/ciężar z wykonanego treningu</strong>
                                        </th>
                                        <th>
                                           <strong>Spełnienie założonej liczby powtórzeń</strong>
                                        </th>
                                     </tr>
                                  </thead>
                                  <tbody>
                                    {
                                        day.targetSeriesRepetitionsDetails.map((item, index) => {
                                            return (
                                                <tr key={item.seriesNumber}>
                                                    <td>{item.seriesNumber}</td>
                                                    <td>{item.targetRepetitionsNumber}</td>
                                                    <td>{item.performedRepetitionsNumber}</td>
                                                    <td>{item.pastWorkoutUsedWeight}</td>
                                                    <td>
                                 <ProgressBar now={Math.floor((item.performedRepetitionsNumber/item.targetRepetitionsNumber)*100)} variant="primary" />
                              </td>
                              <td>
                                 <Badge variant="primary light">{Math.floor((item.performedRepetitionsNumber/item.targetRepetitionsNumber)*100)}%</Badge>
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
                     })}
                    </div>  
                 </div>
              </div> 
           </div> 
                    
           </div>
          );
}