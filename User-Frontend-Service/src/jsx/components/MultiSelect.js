import React from 'react'
import Select from "react-select";

export default function MultiSelect({defValue, data, changeHandler}) {
   
    const CustomClearText = () => "Wyczyść";
    const ClearIndicator = (props) => {
       const {
          children = <CustomClearText />,
          getStyles,
          innerProps: { ref, ...restInnerProps },
       } = props;
       return (
          <div
             {...restInnerProps}
             ref={ref}
             style={getStyles("clearIndicator", props)}
          >
             <div style={{ padding: "0px 5px" }}>{children}</div>
          </div>
       );
    };
    
    const defaultVals = () => {
      if(defValue)
         return []
      else
         return defValue
    }
    
    const ClearIndicatorStyles = (base, state) => ({
       ...base,
       cursor: "pointer",
       color: state.isFocused ? "blue" : "black",
    });

  return (
        <Select
           closeMenuOnSelect={false}
           components={{ ClearIndicator }}
           styles={{ clearIndicator: ClearIndicatorStyles }}
           defaultValue={() => defaultVals}
           isMulti
           options={data}   
           onChange={(e) => changeHandler(e)}    
        />
     );
}