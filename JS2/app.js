fs.readFile('./1.txt', (err, res1) => {
    if(err) {
        console.log('err in 1.txt', err);
    }
    fs.readFile('./2.txt', (err, res2) => {
        if(err) {
            console.log('err in 2.txt', err);
        }
        fs.readFile('./3.txt', (err, res3) => {
            if(err) {
                console.log('err in 3.txt', err);
            }

            let n1 = res1.toString(),
                n2 = res2.toString(),
                n3 = res3.toString(),
                sum;

            sum = (+n1) + (+n2) + (+n3);
            console.log(sum);
        })
    })
})