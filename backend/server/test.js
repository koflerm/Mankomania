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





let a;
a = Math.max.apply(Math, test.map(function(o) {
    return o.dice;
}))
console.log(a)

//const countBy = (d, dice) => d.reduce((r,{dice},i,a) => (r[dice] = a.filter(x => x.dice === dice).length, r),{})
//const counts = countBy(test, 'dice')
//console.log(counts)
console.log(test.filter(x => [x.dice] == a))





