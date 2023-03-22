import React from "react";
import ReactApexChart from "react-apexcharts";

class VolumeRepLine extends React.Component {
   constructor(props) {
      super(props);

      this.state = {
         series: [
            {
               name: this.props.text,
               data: this.props.dates,
            },
         ],
         options: {
            chart: {
               height: 350,
               type: "area",
               toolbar: {
                  show: false,
               },
            },
            dataLabels: {
               enabled: false,
            },

            stroke: {
               width: [4],
               colors: ["#6cc51d"],
               curve: "straight",
            },

            xaxis: {
               type: "text",
               categories: this.props.values,
               
            },
            colors: ["#6cc51d"],
            markers: {
               size: [6],
               strokeWidth: [4],
               strokeColors: ["#6cc51d"],
               border: 0,
               colors: ["#fff"],
               hover: {
                  size: 10,
               },
            },
            yaxis: {
               title: {
                  text: "Objętość [kg]",
               },
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
               type="area"
               height={350}
            />
         </div>
      );
   }
}

export default VolumeRepLine;
