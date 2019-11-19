function contain(arr,value){
    if(arr == null|| value == null){
    	return false;
    }

    for(var i = 0; i < arr.length; i++){
        if(value === arr[i]){
            return true;
        }
    }
    return false;
}