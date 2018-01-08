function mergeArrays(a,b){
    var ret = [];
    for(var k in a){
        if(a.hasOwnProperty(k)) 
            ret[k] = a[k];
    }
    for(var k in b){
        if(b.hasOwnProperty(k)) 
            ret[k] = b[k];
    }
    return ret;
}

function parsAttributesList(attrList){
    if(!attrList) return [];
    var attributesList = [];
    var componentArr = [attrList];
    if(attrList.includes(';'))
        componentArr = attrList.split(';');
    componentArr.forEach(function(comp){
        if(comp && comp.trim()){
            var component  = new Component();
            var compAttrList = comp.split('||');
            component.cid(compAttrList[0]);
            for(j=1;j<compAttrList.length;j++){
                var attrAndVal = compAttrList[j].split('=');
                var attribute = new Attribute();
                attribute.name(attrAndVal[0]);
                attribute.value(attrAndVal[1]);
                component.attributes.push(attribute);
            }
    
            attributesList.push(component);
        }
        
        
    })
    return attributesList;
}

function parsToStringAttributesList(attributesList){
    var attrListString = "";
    if(!attributesList)
        return attrListString;
    attributesList.forEach(function(comp){
        attrListString += comp.cid() + "||";
        comp.attributes().forEach(function(attr){
            attrListString += attr.name() + "=" + attr.value() + "||";
        });
        attrListString = attrListString.substr(0,attrListString.length-2);
        attrListString += ";";
    })
    return attrListString;
}

function getAttributesListFromAttrArrays(attrNames,attrValues)
{
    var attrs = [];
    for(i =0 ; i<attrNames.length;i++)
    {
        if(attrNames[i] && attrNames[i].trim())
        {
            var attribute = new Attribute(attrNames[i].replace(/\s/g, ""),attrValues[i]);
            attrs.push(attribute);
        }
    }

    return attrs;
}

function getMassRequestFromText(text){
    var massRequest = new MassRequest();
    var textAsArray = text.split('\n');
   // console.log(textAsArray);
    massRequest.headerAttributesString(textAsArray[0]);
    massRequest.detailsAttributesString(textAsArray[2]);
    massRequest.linesAttributesString(textAsArray[4]);
    var headerAttrs = textAsArray[0].split(',');
    var headerValues = textAsArray[1].split(',');
    var detailsAttrs = textAsArray[2].split(',');
    var detailsValues = textAsArray[3].split(',');
    var linesAttrs = textAsArray[4].split(',');
    var lines = textAsArray.slice(5);
    
    massRequest.header(getAttributesListFromAttrArrays(headerAttrs,headerValues));

    console.log('header parsed');

    massRequest.details(getAttributesListFromAttrArrays(detailsAttrs,detailsValues));

    console.log('details parsed');

    lines.forEach(function(line){
        
        if(!line || !line.trim()) return;

        var lineValues = line.split(',');
        var attributes = [];
        var retLine = new Line();
        for(i =0 ; i<linesAttrs.length;i++){
            if(linesAttrs[i] == 'AttributesList'){
                retLine.attributesList(parsAttributesList(lineValues[i]));
                retLine.isAttributeListExist(true);
            }
            if(linesAttrs[i] == 'Request Line Number' || linesAttrs[i] == 'RequestLineNumber'){
                retLine.lineNumber(lineValues[i]);
                
            }
            retLine.attributes.push(new Attribute(linesAttrs[i].replace(/\s/g, ""),lineValues[i]));   
        }
        massRequest.lines.push(retLine);
    });

    console.log('lines parsed');

    return massRequest;

}

addValidationToLine = function(line){
    line.attributes().forEach(function(attr){
        var field = $("input[name='"+ attr.name() + "']");
        if(attr.type() == 'date')
        {
            field.attr('type','text');
            field.attr('data-parsley-mindate', "");
            field.datepicker({
                format: "dd/mm/yyyy",
                todayBtn: "linked"
            });
            field.val(attr.value());
        }
        else if(attr.type() == 'cid_list')
        {

            field.attr('type','text');
            field.attr('data-parsley-pattern','([0-9]+;?)*');
        }
        else if(attr.type() == 'cid')
        {

            field.attr('type','text');
            field.attr('data-parsley-pattern','^([0-9]+)$');
        }

        else if(attr.type() == 'attributes_list')
        {

            field.attr('type','text');
            field.attr('data-parsley-attributeslist','');
            
        }

        attr.validations().forEach(function(validation){
            $("input[name='"+ attr.name() + "']").attr('data-parsley-'+validation.name(), validation.value());
        })
        field.on('change',function(){$(this).parsley().validate();});
    })
}

validateAttributesList = function(attrList){
    if(!attrList) return true;
    var retVal = true;
    var componentArr = [attrList];
    if(attrList.includes(';'))
        componentArr = attrList.split(';');
    componentArr.forEach(function(comp){
        if(comp && comp.trim()){
            var compAttrList = comp.split('||');
            if(compAttrList[0].match('[0-9]+')[0] != compAttrList[0]) {
                retVal = false;
            }
            for(j=1;j<compAttrList.length;j++){
                var attrAndVal = compAttrList[j].split('=');
                if(!attrAndVal[0] || !attrAndVal[1] || !attrAndVal[0].trim() || !attrAndVal[1].trim())
                    retVal = false;
            }
        }
    })
    return retVal;
}

parseLineValuesToString = function(line,isValues)
{
    var tempStrAttrs = '';
    line.attributes().forEach(function(attr){
        var val = '';
        if(isValues){
            if(attr.name() == 'AttributesList')
                val = parsToStringAttributesList(line.attributesList());
            else
                val = attr.value();
        }
        else
            val = attr.name();
        tempStrAttrs += val + ',';
    });
    tempStrAttrs += '\r';
    return tempStrAttrs;
}