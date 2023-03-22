import React from 'react'
import { Link } from "react-router-dom";
import { Button, Modal, Dropdown } from "react-bootstrap";

const WorkoutsPagTable = ({workouts, workoutsLoaded}) => {

    if(!workoutsLoaded){
        return <h2>Trwa Wczytywanie planów treningowych...</h2>
    } 

  return (
    <ul className="list-group mb-4">
              {
                              workouts.map((workout, index) => {
                                return  (
                           <div className="d-flex px-3 pt-3 list-row flex-wrap align-items-center mb-2">
                              <div className="list-icon mr-3 mb-3">
                                 <p className="fs-24 text-primary mb-0 mt-2">
                                   { new Date(workout.finishedAt).getMonth() + 1 }.{new Date(workout.finishedAt).getDate()}
                                
                                 </p>
                                 <span className="fs-14 text-primary">{new Date(workout.finishedAt).getFullYear()}</span>
                              </div>
                              <div className="info mb-3">
                                 <h4 className="fs-17">
                           
                                    Plan treningowy: {workout.title}

                                 </h4>
                                 
                                    Dzień treningowy: <span className="text-primary font-w600">   {workout.trainingDay} </span>
                                
                              </div>
                              <div className="d-flex mr-auto mb-3 pl-3 pr-3 align-items-center">
                                 <span className="text-normal ml-2">
                                    Trening  siłowy
                                 </span>
                              </div>
                              <Link
                                 to={`/workouts/${workout.id}`}
                                 className="mb-3 btn btn-primary rounded mr-3"
                              >
                                 <i className="las la-stop scale-2 mr-3" />
                                 Sprawdź szczegóły
                              </Link>
                           </div>
)
                              })
                           }
        </ul>
  )
}

export default WorkoutsPagTable
