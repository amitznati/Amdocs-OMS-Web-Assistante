function Attribute(name,value,type,validationList){
    var self = this;
    self.name = ko.observable(name);
    self.value = ko.observable(value);
    self.type = ko.observable( type || 'text');
    self.validations = ko.observableArray();
    if(validationList){
        validationList.forEach(function(validation){
            self.validations.push(new Validation(validation.name,validation.value));
        })
    }
    self.addValidation = function(){
        self.validations.push(new Validation());
    }
    self.removeValidation = function(validation){
        self.validations.remove(validation);
    }
}

function Validation(name,value){
    var self = this;
    self.name = ko.observable(name);
    self.value = ko.observable(value);

}

function Component(comp){
    var self = this;
    self.cid = ko.observable();
    self.attributes = ko.observableArray();
    if(comp){
        self.cid(comp.cid);
        comp.attributes.forEach(function(attr){
            self.attributes.push(new Attribute(attr.name,attr.value))
        });
    }
    else
    {
        self.attributes.push(new Attribute());
    }
    self.addAttribute = function(){
        self.attributes.push(new Attribute());
    }
    self.deleteAttribute = function(attr){
        if(self.attributes().length != 1)
            self.attributes.remove(attr)
    }
}

function Line(attributes){
    var self = this;
    self.isVisible = ko.observable(false);
    self.attributes = ko.observableArray(attributes);
    self.attributesList = ko.observableArray();
    self.lineNumber = ko.observable();
    self.isAttributeListExist = ko.observable(false);
    self.showLine = function(item, event) {
        self.isVisible(!self.isVisible());
    };

    self.addComp = function(){
        self.attributesList.push(new Component());
    }

    self.deleteComponent = function(comp){
        self.attributesList.remove(comp);
    }

}

function MassValidationLine(line){
    var self = this;
    self.lineName = ko.observable();
    self.lineNumber = ko.observable();
    self.attributes = ko.observableArray();
    self.isLineVisible = ko.observable(false);
    self.exceptedAttribute = null;
    self.attributesList = ko.observableArray();
    self.isAttributeListExist = ko.observable(false);
    self.showLine = function(){
        self.isLineVisible(!self.isLineVisible());
    }
    self.removeAttribute = function(){
        self.attributes.remove(this);
        
    }
    self.addAttribute = function(){
        self.attributes.push(new Attribute());
    }
    self.addComp = function(){
        self.attributesList.push(new Component());
    }

    self.deleteComponent = function(comp){
        self.attributesList.remove(comp);
    }
    if(line){
        line.attributes.forEach(function(attr){
            self.attributes.push(new Attribute(attr.name,attr.value,attr.type,attr.validations));
        })
        if(line.attributesList)
        {
            self.isAttributeListExist(true);
            line.attributesList.forEach(function(comp){
                self.attributesList.push(new Component(comp));
            })
        }
        self.lineName(line.lineName);
        self.lineNumber(line.lineNumber);
        self.exceptedAttribute = line.exceptedAttribute;
    }
}

function ValidationFile(validationFile){
    var self = this;

    self.massHeader = ko.observable(new MassValidationLine(validationFile.massHeader));
    self.massDetails = ko.observable(new MassValidationLine(validationFile.massDetails));
    self.massLines = ko.observableArray();
    validationFile.massLines.forEach(function(line){
        self.massLines.push(new MassValidationLine(line));
    })
    self.requestType = validationFile.requestType;

    self.addLine = function(){
        var line = new MassValidationLine();
        var attributes = [];
        var thiAttrs = self.massLines()[0].attributes();
        thiAttrs.forEach(function(attr)
        {
            var newAttr = new Attribute(attr.name(),attr.value(),attr.type(),null);
            if(attr.name() == 'RequestLineNumber')
            {
                newAttr.value(self.massLines().length+1)
            }
            if(attr.name() == 'AttributesList')
            {
                newAttr.value(null);
            }
            newAttr.validations(attr.validations())
            attributes.push(newAttr);
        });
        line.attributes(attributes);
        line.exceptedAttribute = self.massLines()[0].exceptedAttribute;
        line.lineNumber(self.massLines().length+1);
        line.lineName('Mass Lines');
        line.isAttributeListExist(true);
        self.massLines.push(line);
        addValidationToLine(line);
    };
    self.deleteLine = function(line){
        if(self.massLines().length > 1)
            self.massLines.remove(line);
    }

    self.getAllRows = function(){
        var allRows = [];
        allRows.push(parseLineValuesToString(self.massHeader(),false));
        allRows.push(parseLineValuesToString(self.massHeader(),true));
        allRows.push(parseLineValuesToString(self.massDetails(),false));
        allRows.push(parseLineValuesToString(self.massDetails(),true));
        allRows.push(parseLineValuesToString(self.massLines()[0],false));
        self.massLines().forEach(function(line){
            allRows.push(parseLineValuesToString(line,true));
        })

        return allRows;
    }
    
}

function MassRequest(){
    self = this;
    self.header = ko.observableArray();
    self.headerAttributesString = ko.observable();
    self.details = ko.observableArray();
    self.detailsAttributesString = ko.observable();
    self.lines = ko.observableArray();
    self.linesAttributesString = ko.observable();
    self.isHeaderVisible = ko.observable(false);
    self.isDetailsVisible = ko.observable(false);
    

    self.showHeader = function(){ self.isHeaderVisible(!self.isHeaderVisible());}

    self.showDetails = function(){self.isDetailsVisible(!self.isDetailsVisible());}

    self.deleteLine = function(line){self.lines.remove(line);}

    self.addLine = function(){
        var attrs = [];
        var newLine = new Line([]);
        
        self.linesAttributesString().split(',').forEach(function(attrName){
            var val = "";
            if(attrName == 'Request Line Number' || attrName == 'RequestLineNumber' ){
                newLine.lineNumber(self.lines().length+1);
                val = self.lines().length+1;
            }
            newLine.attributes.push(new Attribute(attrName,val));
        });
        newLine.isVisible(true);
        self.lines.push(newLine);
    };


    self.parsLine = function(line){
        var retVal = "";
        line.forEach(function(attr){
            retVal += attr.value() + ",";   
        });
        retVal = retVal.substr(0,retVal.length-1);
        return retVal;
    }

    self.getAllRows = function(){
        var allRows = [];   
        allRows.push(self.headerAttributesString());
        allRows.push(self.parsLine(self.header()));           
        allRows.push(self.detailsAttributesString());
        allRows.push(self.parsLine(self.details()));
        allRows.push(self.linesAttributesString());
        self.lines().forEach(function(line){
            allRows.push(self.parsLine(line.attributes()));
        });
        
        return allRows;
    };

}