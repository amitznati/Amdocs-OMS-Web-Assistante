function Attribute(name,value,type){
    var self = this;
    self.name = ko.observable(name);
    self.value = ko.observable(value);
    self.type = ko.observable( type || 'text');
    self.validation = ko.observable();

}

function Component(){
    var self = this;
    self.cid = ko.observable();
    self.attributes = ko.observableArray();
    self.addAttribute = function(){
        self.attributes.push(new Attribute());
    }
    self.deleteAttribute = function(attr){
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
        console.log('show line');
    };

    self.addComp = function(){
        self.attributesList.push(new Component());
    }

    self.deleteComponent = function(comp){
        self.attributesList.remove(comp);
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
    

    self.showHeader = function(){console.log('show header'); self.isHeaderVisible(!self.isHeaderVisible());}

    self.showDetails = function(){self.isDetailsVisible(!self.isDetailsVisible());}

    self.deleteLine = function(line){console.log('delete line: '+line); self.lines.remove(line);}

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