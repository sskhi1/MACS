# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent


class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """

    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices)  # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        result = 0.0
        ghost_positions = successorGameState.getGhostPositions()
        if successorGameState.isWin():
            return 10000
        if successorGameState.isLose():
            return -10000

        for p in ghost_positions:
            if p == newPos or manhattanDistance(newPos, p) < 2:
                return -10000

        food_list = newFood.asList()
        if currentGameState.getNumFood() > len(food_list):
            return 10000

        for food in food_list:
            distance = manhattanDistance(newPos, food)
            if distance < 2:
                return 10000
            elif distance < 5:
                result += 5
            elif distance < 10:
                result += 2.5
            elif distance < 20:
                result += 1
            else:
                result += 0.1

        return successorGameState.getScore() + result


def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()


class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn='scoreEvaluationFunction', depth='2'):
        self.index = 0  # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)
        self.positive_infinity = float('inf')


class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        return self.minimax(gameState, self.index, 0)[1]

    def minimax(self, gameState, agent_idx, depth):
        result = ()
        actions = gameState.getLegalActions()
        if self.is_game_ended(actions, depth, gameState):
            return self.evaluationFunction(gameState), 0

        next_agent_idx = self.index if agent_idx == gameState.getNumAgents() - 1 else agent_idx + 1
        depth += 1 if agent_idx == gameState.getNumAgents() - 1 else 0

        for action in gameState.getLegalActions(agent_idx):
            value = self.minimax(gameState.generateSuccessor(agent_idx, action), next_agent_idx, depth)[0]

            if self.should_update_result(agent_idx, result, value):
                result = (value, action)

        return result

    def should_update_result(self, agent_idx, result, value):
        return not result or (agent_idx == self.index and value > result[0]) or (
                agent_idx != self.index and value < result[0])

    def is_game_ended(self, actions, depth, gameState):
        return gameState.isWin() or gameState.isLose() or len(actions) == 0 or depth == self.depth


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        positive_infinity = float('inf')
        return self.alphabeta(gameState, self.index, 0, -positive_infinity, positive_infinity)[1]

    def alphabeta(self, gameState, agent_idx, depth, alpha, beta):
        result = ()
        actions = gameState.getLegalActions()
        if self.is_game_ended(actions, depth, gameState):
            return self.evaluationFunction(gameState), 0

        next_agent_idx = self.index if agent_idx == gameState.getNumAgents() - 1 else agent_idx + 1
        depth += 1 if agent_idx == gameState.getNumAgents() - 1 else 0

        for action in gameState.getLegalActions(agent_idx):
            if result:
                if (agent_idx == self.index and result[0] > beta) or (agent_idx != self.index and result[0] < alpha):
                    return result
            value = self.alphabeta(gameState.generateSuccessor(agent_idx, action), next_agent_idx, depth,
                                   alpha, beta)[0]

            if self.should_update_result(agent_idx, result, value):
                result = (value, action)
                if agent_idx == self.index and result:
                    alpha = max(result[0], alpha)
                elif agent_idx != self.index and result:
                    beta = min(result[0], beta)

        return result

    def should_update_result(self, agent_idx, result, value):
        return not result or (agent_idx == self.index and value > result[0]) or (
                agent_idx != self.index and value < result[0])

    def is_game_ended(self, actions, depth, gameState):
        return gameState.isWin() or gameState.isLose() or len(actions) == 0 or depth == self.depth


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        return self.expectimax(gameState, self.index, 0)[1]

    def expectimax(self, gameState, agent_idx, depth):
        result = ()
        actions = gameState.getLegalActions()
        if self.is_game_ended(actions, depth, gameState):
            return self.evaluationFunction(gameState), 0

        next_agent_idx = self.index if agent_idx == gameState.getNumAgents() - 1 else agent_idx + 1
        depth += 1 if agent_idx == gameState.getNumAgents() - 1 else 0

        for action in gameState.getLegalActions(agent_idx):
            value = self.expectimax(gameState.generateSuccessor(agent_idx, action), next_agent_idx, depth)[0]

            result = self.calculate_curr_result(action, agent_idx, gameState, result, value)
        return result

    def calculate_curr_result(self, action, agent_idx, gameState, result, value):
        if agent_idx == self.index and (not result or result[0] < value):
            result = (value, action)
        elif agent_idx != self.index:
            if result:
                result = (result[0] + (1.0 / len(gameState.getLegalActions(agent_idx))) * value, action)
            else:
                result = ((1.0 / len(gameState.getLegalActions(agent_idx))) * value, action)
        return result

    def is_game_ended(self, actions, depth, gameState):
        return gameState.isWin() or gameState.isLose() or len(actions) == 0 or depth == self.depth


def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    score = 0.0
    if currentGameState.isWin():
        return 10000
    if currentGameState.isLose():
        return -10000

    pacman_position = currentGameState.getPacmanPosition()
    food_grid = currentGameState.getFood()
    food_list = food_grid.asList()
    ghost_states = currentGameState.getGhostStates()
    capsules = currentGameState.getCapsules()

    score += -len(food_list) - len(capsules)

    ghosts = [ghost for ghost in ghost_states]
    ghosts_dist = [manhattanDistance(pacman_position, ghost.getPosition()) for ghost in ghosts]
    for dist in ghosts_dist:
        score += -4 * dist

    return currentGameState.getScore() + score


# Abbreviation
better = betterEvaluationFunction
