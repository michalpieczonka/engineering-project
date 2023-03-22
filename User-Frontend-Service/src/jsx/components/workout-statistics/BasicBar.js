import React from "react";

import ReactApexChart from "react-apexcharts";

class BasicBar extends React.Component {
    constructor(props) {
       super(props);
 
       this.state = {
          series: [
             {
                name: this.props.text,
                data: this.props.xdata,
             },
          ],
          options: {
             chart: {
                type: "bar",
                height: 230,
                toolbar: {
                   show: false,
                },
             },
             plotOptions: {
                bar: {
                   horizontal: false,
                   dataLabels: {
                      position: "top",
                   },
                },
             },
             colors:['#6cc51d'],
             legend: {
                show: false,
                position: "top",
                horizontalAlign: "left",
             },
             dataLabels: {
                enabled: false,
                offsetX: -6,
                style: {
                   fontSize: "12px",
                   colors: ["#fff"],
                },
             },
             stroke: {
                show: false,
             },
             yaxis: {
                lines: {
                   show: false,
                },
             },
             xaxis: {
                show: false,
                categories: this.props.ydata,
             },
             
          },
       };
    }
 
    render() {
       return (
          <div id="chart">
             <ReactApexChart
                options={this.state.options}
                series={this.state.series}
                type="bar"
                height={350}
                
             />
          </div>
       );
    }
 }
 export default BasicBar;
 