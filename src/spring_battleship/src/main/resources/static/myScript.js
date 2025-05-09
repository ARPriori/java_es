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

    loadGameState();

    // Carica lo stato del gioco
    function loadGameState() {
        $.ajax({
            url: '/api/popola-griglie',
            method: 'GET',
            success: function (response) {
                // Mostra navi computer (debug)
                response.computerShips.forEach(ship => {
                    ship.forEach(index => {
                        $('#computer-grid .cell').eq(index).addClass('ship');
                    })
                });

                // Se il gioco è già iniziato, carica lo stato
                if (response.gameStarted) {
                    gameStarted = true;
                    $('#placement-phase').hide();
                    $('#game-phase').show();
                    updateGameState();
                }
            }
        });
    }


    // Posiziona navi del giocatore
    $('#randomize-ships').click(function () {

        const button = $(this);
        button.prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status"></span> Randomizing...');

        $.ajax({
            url: '/api/randomizza-player',
            method: 'GET',
            success: function (response) {
                // Mostra navi player randomizzate
                response.playerShips.forEach(ship => {
                    ship.forEach(index => {
                        $('#player-grid .cell').eq(index).addClass('ship');
                    })
                });

                // Aggiorna stato e UI
                playerShips = response.playerShips.flat();
                button.text('Randomize Ships');

            }, error: function () {
                alert('Errore durante il posizionamento delle navi');
                button.prop('disabled', false).text('Randomize Ships');
            }
        })

    });

    // Attacca
    $('#computer-grid').on('click', '.cell', function () {
        if (!gameStarted || gameOver) return;

        const index = $(this).data('index');
        const cell = $(this);

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
                    if (response.sunk) {
                        cell.addClass('sunk');
                    }
                } else {
                    cell.addClass('miss');
                }

                // Aggiorna griglia giocatore (attacco computer)
                if (response.computerHit !== undefined) {
                    // Dovresti ricevere le coordinate dell'attacco computer
                    // e aggiornare la griglia del giocatore
                }

                // Controlla fine gioco
                if (response.gameOver) {
                    gameOver = true;
                    alert(`Game Over! Vincitore: ${response.winner}`);
                }
            },
            error: function (xhr) {
                alert('Errore: ' + xhr.responseJSON.error);
            }
        });
    });

    // Resetta il gioco
    $('#reset-game').click(function () {
        $.ajax({
            url: '/api/reset',
            method: 'POST',
            success: function () {
                location.reload();
            }
        });
    });

    // Aggiorna lo stato del gioco
    function updateGameState() {
        $.ajax({
            url: '/api/stato-gioco',
            method: 'GET',
            success: function (response) {
                // Aggiorna griglia giocatore
                response.playerBoard.hitCells.forEach(index => {
                    $('#player-grid .cell').eq(index).addClass('hit');
                });

                // Aggiorna griglia computer
                response.computerBoard.hitCells.forEach(index => {
                    $('#computer-grid .cell').eq(index).addClass('hit');
                });

                if (response.gameOver) {
                    gameOver = true;
                    alert(`Game Over! Vincitore: ${response.winner}`);
                }
            }
        });
    }
});