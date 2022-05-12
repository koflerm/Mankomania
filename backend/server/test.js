const temp = {
    socket: "1",
    dice : 6
}

const temp2 = {
    socket: "2",
    dice : 6
}

const temp3 = {
    socket: "3",
    dice : 4
}

const temp4= {
    socket: "4",
    dice : 3
}

const test =  []

test.push(temp)
test.push(temp2)
test.push(temp3)
test.push(temp4)






console.log(Math.max.apply(Math, test.map(function(o) {
    return o.dice;
})))

for (let s of test){
    if (s.dice === 6){
        console.log(s.socket)
    }
}



