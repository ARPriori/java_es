$(document).ready(function () {
    let gameStarted = false;
    let gameOver = false;
    let playerShips = [];

    function createEmptyGrid(container) {
        $(container).empty();
        for (let i = 0; i < 100; i++) {
            $(container).append(`<div class="cell" data-index="${i}"></div>`);
        }
    }

    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    loadGame();

    // CARICA GIOCO
    function loadGame() {
        $.ajax({
            url: '/api/popola-griglie',
            method: 'GET',
            success: function (response) {
                gameStarted = true;

                // Mostra navi computer (debug)
                response.computerShips.forEach(ship => {
                    ship.forEach(index => {
                        $('#computer-grid .cell').eq(index).addClass('computer-ship');
                    })
                });
            }
        });
    }

    // POSIZIONA NAVI GIOCATORE
    $('#randomize-ships').click(function () {

        const button = $(this);
        button.prop('disabled', true);


        $('#reset-game').prop('disabled', false);

        $.ajax({
            url: '/api/randomizza-player',
            method: 'GET',
            success: function (response) {
                // Mostra navi player randomizzate
                response.playerShips.forEach((ship, shipIndex) => {
                    const shipColor = getShipColor(shipIndex); // Funzione per ottenere il colore della nave
                    ship.forEach(index => {
                        $('#player-grid .cell').eq(index).addClass('ship').css('background-color', shipColor);
                    });
                });

                // Aggiorna stato e UI
                playerShips = response.playerShips.flat();

            }, error: function () {
                alert('Errore durante il posizionamento delle navi');
                button.prop('disabled', false).text('Randomize Ships');
            }
        })
    });

    // CREA COLORI NAVI
    function getShipColor(shipIndex) {
        const colors = [];

        let val = 222;
        for (let i = 0; i < 9; i++) {
            colors[i] = 'rgb(252, ' + val + ', 252)';
            val -= 10;
        }

        return colors[shipIndex % colors.length]; // Usa un colore in base all'indice della nave
    }

    // ATTACCA
    $('#computer-grid').on('click', '.cell', function () {

        if (!gameStarted || gameOver) return;

        const cell = $(this);
        const index = cell.data('index');
        console.log("Cella cliccata " + index);

        if (cell.hasClass('hit') || cell.hasClass('miss')) {
            return;
        }

        $.ajax({
            url: '/api/attacca/' + index,
            method: 'PUT',
            success: function (response) {
                // Aggiorna griglia computer
                if (response.hit) {
                    cell.addClass('hit');
                    $('#gameStatus').text(`Player ha colpito una barca del Computer`);
                    if (response.sunk) {
                        cell.addClass('sunk');
                        $('#gameStatus').text(`Player ha affondato una barca del Computer`);
                    }
                } else {
                    cell.addClass('miss');
                    $('#gameStatus').text(`Player ha missato`);
                }

                // Aggiorna griglia giocatore (attacco computer)
                if (response.computerHit !== undefined) {
                    const playerHitCells = $('#player-grid .cell');
                    response.playerHitIndex && playerHitCells.eq(response.playerHitIndex).addClass(response.computerHit ? 'hit' : 'miss');
                }

                // Controlla fine gioco
                if (response.gameOver) {
                    gameOver = true;
                    $('#gameStatus').text(`Game Over! Vincitore: ${response.winner}`);
                }
            },
            error: function (xhr) {
                $('#gameStatus').text('Errore: ' + xhr.responseJSON.error).addClass("text-danger");
            }
        });
    });



    // RESETTA IL GIOCO
    $('#reset-game').click(function () {
        $.ajax({
            url: '/api/reset',
            method: 'POST',
            success: function () {
                location.reload();
            }
        });
    });
});