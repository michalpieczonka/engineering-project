import React from 'react'
import {
    Table,
 } from "react-bootstrap";
 import { Link } from "react-router-dom";

export default function CreateWorkout() {

    return (
        
        <div className="row">
           <div className="col-lg-12">
              <div className="card">
                 <div className="card-header">
                    <h4 className="card-title">Form Validation</h4>
                 </div>
                 <div className="card-body">
                    <h2> tutaj ludzik </h2>
                 </div>
              </div>
           </div>
           <div className="col-lg-12">
              <div className="card">
                 <div className="card-header">
                    <h4 className="card-title">Szkielet twojego nowego planu treningowe</h4>
                 </div>
                 <div className="card-body">

                 <Table responsive>
                        <thead>
                           <tr>
                              <th className="width50">
                                 <div className="custom-control custom-checkbox checkbox-success check-lg mr-3">
                                    <input
                                       type="checkbox"
                                       className="custom-control-input"
                                       id="checkAll"
                                       required=""
                                    />
                                    <label
                                       className="custom-control-label"
                                       htmlFor="checkAll"
                                    ></label>
                                 </div>
                              </th>
                              <th>
                                 <strong>Numer ćwiczenia.</strong>
                              </th>
                              <th>
                                 <strong>Nazwa ćwiczenia</strong>
                              </th>
                              <th>
                                 <strong>Liczba serii</strong>
                              </th>
                              <th>
                                 <strong>Liczba powtórzeń</strong>
                              </th>
                              <th>
                                 <strong>Operacje dodatkowe</strong>
                              </th>
                           </tr>
                        </thead>
                        <tbody>
                           <tr>
                              <td>
                                 <div className="custom-control custom-checkbox checkbox-success check-lg mr-3">
                                    <input
                                       type="checkbox"
                                       className="custom-control-input"
                                       id="customCheckBox2"
                                       required=""
                                    />
                                    <label
                                       className="custom-control-label"
                                       htmlFor="customCheckBox2"
                                    ></label>
                                 </div>
                              </td>
                              <td>
                                 <strong>542</strong>
                              </td>
                              <td>example@example.com </td>
                              <td>01 August 2020</td>
                              <td>
                                 <div className="d-flex align-items-center">
                                    <i className="fa fa-circle text-success mr-1"></i>{" "}
                                    Successful
                                 </div>
                              </td>
                              <td>
                                 <div className="d-flex">
                                    <Link
                                       href="#"
                                       className="btn btn-primary shadow btn-xs sharp mr-1"
                                    >
                                       <i className="fa fa-pencil"></i>
                                    </Link>
                                    <Link
                                       href="#"
                                       className="btn btn-danger shadow btn-xs sharp"
                                    >
                                       <i className="fa fa-trash"></i>
                                    </Link>
                                 </div>
                              </td>
                           </tr>
                           <tr>
                              <td>
                                 <div className="custom-control custom-checkbox checkbox-success check-lg mr-3">
                                    <input
                                       type="checkbox"
                                       className="custom-control-input"
                                       id="customCheckBox3"
                                       required=""
                                    />
                                    <label
                                       className="custom-control-label"
                                       htmlFor="customCheckBox3"
                                    ></label>
                                 </div>
                              </td>
                              <td>
                                 <strong>542</strong>
                              </td>
                              <td>example@example.com </td>
                              <td>01 August 2020</td>
                              <td>
                                 <div className="d-flex align-items-center">
                                    <i className="fa fa-circle text-danger mr-1"></i>{" "}
                                    Canceled
                                 </div>
                              </td>
                              <td>
                                 <div className="d-flex">
                                    <Link
                                       href="#"
                                       className="btn btn-primary shadow btn-xs sharp mr-1"
                                    >
                                       <i className="fa fa-pencil"></i>
                                    </Link>
                                    <Link
                                       href="#"
                                       className="btn btn-danger shadow btn-xs sharp"
                                    >
                                       <i className="fa fa-trash"></i>
                                    </Link>
                                 </div>
                              </td>
                           </tr>
                           <tr>
                              <td>
                                 <div className="custom-control custom-checkbox checkbox-success check-lg mr-3">
                                    <input
                                       type="checkbox"
                                       className="custom-control-input"
                                       id="customCheckBox4"
                                       required=""
                                    />
                                    <label
                                       className="custom-control-label"
                                       htmlFor="customCheckBox4"
                                    ></label>
                                 </div>
                              </td>
                              <td>
                                 <strong>542</strong>
                              </td>
                              <td>example@example.com </td>
                              <td>01 August 2020</td>
                              <td>
                                 <div className="d-flex align-items-center">
                                    <i className="fa fa-circle text-warning mr-1"></i>{" "}
                                    Pending
                                 </div>
                              </td>
                              <td>
                                 <div className="d-flex">
                                    <Link
                                       href="#"
                                       className="btn btn-primary shadow btn-xs sharp mr-1"
                                    >
                                       <i className="fa fa-pencil"></i>
                                    </Link>
                                    <Link
                                       href="#"
                                       className="btn btn-danger shadow btn-xs sharp"
                                    >
                                       <i className="fa fa-trash"></i>
                                    </Link>
                                 </div>
                              </td>
                           </tr>
                        </tbody>
                     </Table>

                 </div>
              </div>
           </div>
        </div>
 
       );
}
